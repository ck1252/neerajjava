package com.vpcons.nsereportsystem.dto.nse.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NSEBankBalanceRequest {
    @JsonProperty("BankBalanceData")
    private Object bankBalanceData;
}
