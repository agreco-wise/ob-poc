package com.transferwise.obpoc.details;

import org.jose4j.jws.AlgorithmIdentifiers;

public interface Details {

  String getAuthorisationUrl();

  String getAuthorisationIssuerUrl();

  String getClientId();

  String getSigningKeyId();

  default String getSigningAlgorithm() {
    return AlgorithmIdentifiers.RSA_PSS_USING_SHA256;
  }
}
