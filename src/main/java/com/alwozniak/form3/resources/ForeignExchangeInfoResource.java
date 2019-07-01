package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ForeignExchangeInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public ForeignExchangeInfoResource() {
        // For Jackson.
    }

    @JsonProperty("contract_reference")
    public String getContractReference() {
        return contractReference;
    }

    @JsonProperty("contract_reference")
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    @JsonProperty("exchange_rate")
    public String getExchangeRateString() {
        return exchangeRate == null ? null : String.format("%.5f", exchangeRate);
    }

    @JsonProperty("exchange_rate")
    public void setExchangeRateFromString(String exchangeRateString) {
        this.exchangeRate = Double.valueOf(exchangeRateString);
    }

    @JsonIgnore
    public Double getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @JsonProperty("original_amount")
    public String getOriginalAmountString() {
        return originalAmount == null ? null : String.format("%.2f", originalAmount);
    }

    @JsonProperty("original_amount")
    public void setOriginalAmountFromString(String amountString) {
        this.originalAmount = Double.valueOf(amountString);
    }

    @JsonIgnore
    public Double getOriginalAmount() {
        return this.originalAmount;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    @JsonProperty("original_currency")
    public String getOriginalCurrency() {
        return originalCurrency;
    }

    @JsonProperty("original_currency")
    public void setOriginalCurrency(String currency) {
        this.originalCurrency = currency;
    }

}
