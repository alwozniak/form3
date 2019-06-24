package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ForeignExchangeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ForeignExchangeInfoResource {
    private ForeignExchangeInfo foreignExchangeInfo;

    public ForeignExchangeInfoResource(ForeignExchangeInfo foreignExchangeInfo) {
        this.foreignExchangeInfo = foreignExchangeInfo;
    }

    @JsonProperty("contract_reference")
    public String getContractReference() {
        return foreignExchangeInfo.getContractReference();
    }

    @JsonProperty("exchange_rate")
    public String getExchangeRate() {
        Double exchangeRate = foreignExchangeInfo.getExchangeRate();
        return exchangeRate == null ? null : String.format("%.5f", exchangeRate);
    }

    @JsonProperty("original_amount")
    public String getOriginalAmount() {
        Double originalAmount = foreignExchangeInfo.getOriginalAmount();
        return originalAmount == null ? null : String.format("%.2f", originalAmount);
    }

    @JsonProperty("original_currency")
    private String getOriginalCurrency() {
        return foreignExchangeInfo.getOriginalCurrency();
    }
}
