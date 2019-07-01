package com.alwozniak.form3.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentsListResource extends PaymentsResource {

    private final List<PaymentResourceData> payments;

    public PaymentsListResource(List<PaymentResourceData> payments) {
        super();
        this.payments = payments;
    }

    @JsonProperty("data")
    public List<PaymentResourceData> getData() {
        return payments;
    }
}
