package com.transferwise.obpoc.details;

public class MonzoBankDetails implements Details {

  @Override
  public String getAuthorisationUrl() {
    return "https://verify.monzo.com/open-banking/authorize";
  }

  @Override
  public String getAuthorisationIssuerUrl() {
    return "https://api.monzo.com/open-banking/";
  }

  @Override
  public String getClientId() {
    return "oauth2client_0000A9boe5yUUpGunr9TF3";
  }

  @Override
  public String getSigningKeyId() {
    return "8X2jYGh-1vXXcHH1rhusuUuzDzg";
  }
}
