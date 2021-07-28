package com.transferwise.obpoc.details;

public class HsbcBankDetails implements Details {

  @Override
  public String getAuthorisationUrl() {
    return "https://ob.hsbc.co.uk/obie/open-banking/v1.1/oauth2/authorize";
  }

  @Override
  public String getAuthorisationIssuerUrl() {
    return "https://api.ob.hsbc.co.uk";
  }

  @Override
  public String getClientId() {
    return "0d19f9c4-9e80-41b0-ad52-0f1da5eab600";
  }


  @Override
  public String getSigningKeyId() {
    return "8X2jYGh-1vXXcHH1rhusuUuzDzg";
  }
}
