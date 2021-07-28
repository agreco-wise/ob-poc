package com.transferwise.obpoc.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthorisationRequestJwtClaims extends JwtClaims {

  @JsonProperty("nonce")
  private String nonce;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("response_type")
  private String responseType;

  @JsonProperty("client_id")
  private String clientId;

  @JsonProperty("redirect_uri")
  private String redirectUri;

  @JsonProperty("state")
  private String state;

  @JsonProperty("claims")
  private AuthorisationRequestClaims claims;
}
