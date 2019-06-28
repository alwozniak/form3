package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ChargeInfoForCurrency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChargeInfoForCurrencyResource {

    private Double amount;
    private String currency;

    public ChargeInfoForCurrencyResource(ChargeInfoForCurrency chargeInfoForCurrency) {
        this.amount = chargeInfoForCurrency.getAmount();
        this.currency = chargeInfoForCurrency.getCurrency();
    }

    public ChargeInfoForCurrencyResource(@JsonProperty("amount") String amountString,
                                         @JsonProperty("currency") String currency) {
        this.amount = Double.valueOf(amountString);
        this.currency = currency;
    }

    @JsonProperty("amount")
    public String getAmountString() {
        return amount == null ? null : String.format("%.2f", amount);
    }

    @JsonIgnore
    public Double getAmount() {
        return this.amount;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }
}
