package com.transferwise.obpoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transferwise.obpoc.details.Details;
import com.transferwise.obpoc.details.MonzoBankDetails;
import com.transferwise.obpoc.jwt.AuthorisationRequestClaims;
import com.transferwise.obpoc.jwt.AuthorisationRequestJwtClaims;
import com.transferwise.obpoc.jwt.Claim;
import com.transferwise.obpoc.jwt.IdToken;
import com.transferwise.obpoc.jwt.UserInfo;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class Main {

  private static final String UTF_8_CHARSET = StandardCharsets.UTF_8.name();
  private static final String CONSENT = "obaispaccountinformationconsent_0000A9k73bZlUejel6cwwz"; //TODO change with the new consent created
  private static final String KEY_PATH = "/Users/andrea.greco/Documents/openbanking-aisp/keys/obseal.key"; // TODO change with the correct path

  public static void main(String[] args) throws Exception {
    System.out.println("Test - " + generateRedirectUrl());
  }

  public static String generateRedirectUrl() throws Exception {

    String nonce = UUID.randomUUID().toString();
    String redirectUrl = "https://wise.com/openbanking/aisp-redirect-test-3";

    MonzoBankDetails monzoBankDetailsProd = new MonzoBankDetails();

    String requestToken = generateRequestToken(monzoBankDetailsProd, nonce, redirectUrl);

    try {
      UriComponents uriComponents = UriComponentsBuilder.fromUriString(monzoBankDetailsProd.getAuthorisationUrl())
          .queryParam("client_id", URLEncoder.encode(monzoBankDetailsProd.getClientId(), UTF_8_CHARSET))
          .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, UTF_8_CHARSET))
          .queryParam("response_type", URLEncoder.encode("code id_token", UTF_8_CHARSET))
          .queryParam("scope", URLEncoder.encode("openid accounts", UTF_8_CHARSET))
          .queryParam("nonce", URLEncoder.encode(nonce, UTF_8_CHARSET))
          .queryParam("state", URLEncoder.encode("STATE", UTF_8_CHARSET))
          .queryParam("request", URLEncoder.encode(requestToken, UTF_8_CHARSET))
          .build(true);

      return uriComponents.toUriString();
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Error generating redirect URL", e);
    }
  }

  private static String generateRequestToken(MonzoBankDetails bankDetails,
                                             String nonce,
                                             String redirectUrl) throws Exception {

    AuthorisationRequestJwtClaims jwtClaims = new AuthorisationRequestJwtClaims();


    jwtClaims.setIssuer(bankDetails.getClientId());
    jwtClaims.setAudience(bankDetails.getAuthorisationIssuerUrl());
    jwtClaims.setIssuedAt(Instant.now().getEpochSecond());
    jwtClaims.setExpires(Instant.now().plus(Duration.ofMinutes(30)).getEpochSecond());
    jwtClaims.setJwtId(UUID.randomUUID().toString());
    jwtClaims.setNonce(nonce);
    jwtClaims.setScope("openid accounts");
    jwtClaims.setResponseType("code id_token");
    jwtClaims.setClientId(bankDetails.getClientId());
    jwtClaims.setRedirectUri(redirectUrl);
    jwtClaims.setState("STATE");

    Claim openBankingIntentId = Claim.builder()
        .essential(true)
        .value(CONSENT)
        .build();

    Claim authenticationContextClassReference = Claim.builder()
        .essential(true)
        .values(List.of("urn:openbanking:psd2:sca"))
        .build();

    IdToken idToken = IdToken.builder()
        .openBankingIntentId(openBankingIntentId)
        .authenticationContextClassReference(authenticationContextClassReference)
        .build();

    UserInfo userInfo = UserInfo.builder()
        .openBankingIntentId(openBankingIntentId)
        .build();

    AuthorisationRequestClaims authorisationRequestClaims = AuthorisationRequestClaims.builder()
        .idToken(idToken)
        .userInfo(userInfo)
        .build();

    ObjectMapper objectMapper = new ObjectMapper();

    jwtClaims.setClaims(authorisationRequestClaims);

    return signJsonPayload(objectMapper.writeValueAsString(jwtClaims), bankDetails);
  }

  private static String signJsonPayload(String payload, Details details) throws Exception {
    JsonWebSignature jsonWebSignature = new JsonWebSignature();
    jsonWebSignature.setPayload(payload);
    PrivateKey privateKey = getKeyCert();
    jsonWebSignature.setKey(privateKey);
    jsonWebSignature.setAlgorithmHeaderValue(details.getSigningAlgorithm());
    jsonWebSignature.setKeyIdHeaderValue(details.getSigningKeyId());

    try {
      return jsonWebSignature.getCompactSerialization();
    } catch (JoseException e) {
      throw new Exception("Unable to create JSON web signature", e);
    }
  }

  public static PrivateKey getKeyCert()
      throws Exception {

    String privateKeyAsString = Files.readString(Paths.get(KEY_PATH));

    String publicKeyPem = privateKeyAsString
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replaceAll("\\n", "")
        .replace("-----END PRIVATE KEY-----", "");

    byte[] keyBytes = Base64.getDecoder().decode(publicKeyPem);
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
  }
}
