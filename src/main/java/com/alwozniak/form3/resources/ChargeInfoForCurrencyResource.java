package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ChargeInfoForCurrency;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChargeInfoForCurrencyResource {

    private Double amount;
    private String currency;

    public ChargeInfoForCurrencyResource(ChargeInfoForCurrency chargeInfoForCurrency) {
        this.amount = chargeInfoForCurrency.getAmount();
        this.currency = chargeInfoForCurrency.getCurrency();
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount == null ? null : String.format("%.2f", amount);
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }
}
