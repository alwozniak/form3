package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ForeignExchangeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ForeignExchangeInfoResource {

    private String contractReference;
    private Double exchangeRate;
    private Double originalAmount;
    private String originalCurrency;

    public ForeignExchangeInfoResource(ForeignExchangeInfo foreignExchangeInfo) {
        this.contractReference = foreignExchangeInfo.getContractReference();
        this.exchangeRate = foreignExchangeInfo.getExchangeRate();
        this.originalAmount = foreignExchangeInfo.getOriginalAmount();
        this.originalCurrency = foreignExchangeInfo.getOriginalCurrency();
    }

    @JsonProperty("contract_reference")
    public String getContractReference() {
        return contractReference;
    }

    @JsonProperty("exchange_rate")
    public String getExchangeRate() {
        return exchangeRate == null ? null : String.format("%.5f", exchangeRate);
    }

    @JsonProperty("original_amount")
    public String getOriginalAmount() {
        return originalAmount == null ? null : String.format("%.2f", originalAmount);
    }

    @JsonProperty("original_currency")
    private String getOriginalCurrency() {
        return originalCurrency;
    }
}
