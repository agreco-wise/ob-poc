package com.transferwise.obpoc.jwt;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
  @JsonProperty("openbanking_intent_id")
  private Claim openBankingIntentId;
}
