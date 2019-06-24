package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ChargeInfoForCurrency;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChargeInfoForCurrencyResource {
    private ChargeInfoForCurrency chargeInfoForCurrency;

    public ChargeInfoForCurrencyResource(ChargeInfoForCurrency chargeInfoForCurrency) {
        this.chargeInfoForCurrency = chargeInfoForCurrency;
    }

    @JsonProperty("amount")
    public String getAmount() {
        Double amount = chargeInfoForCurrency.getAmount();
        return amount == null ? null : String.format("%.2f", amount);
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return chargeInfoForCurrency.getCurrency();
    }
}
