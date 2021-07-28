package com.transferwise.obpoc.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jose4j.jwt.ReservedClaimNames;

@Data
public class JwtClaims {

  @JsonProperty(ReservedClaimNames.ISSUER)
  private String issuer;

  @JsonProperty(ReservedClaimNames.SUBJECT)
  private String subject;

  @JsonProperty(ReservedClaimNames.AUDIENCE)
  private String audience;

  @JsonProperty(ReservedClaimNames.ISSUED_AT)
  private long issuedAt;

  @JsonProperty(ReservedClaimNames.EXPIRATION_TIME)
  private long expires;

  @JsonProperty(ReservedClaimNames.JWT_ID)
  private String jwtId;
}
