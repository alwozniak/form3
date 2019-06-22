package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaymentsListResource extends PaymentsResource {

    private final List<FinancialTransaction> payments;

    public PaymentsListResource(List<FinancialTransaction> payments) {
        super();
        this.payments = payments;
    }

    @JsonProperty("data")
    public List<FinancialTransaction> getData() {
        return payments;
    }
}
