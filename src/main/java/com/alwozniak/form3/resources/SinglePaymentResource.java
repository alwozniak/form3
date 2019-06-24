package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SinglePaymentResource extends PaymentsResource {

    private final FinancialTransaction payment;

    public SinglePaymentResource(FinancialTransaction payment) {
        this.payment = payment;
    }

    @JsonProperty("data")
    public PaymentResourceData getData() {
        return new PaymentResourceData(payment);
    }
}
