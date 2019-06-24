package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class PaymentResourceData {

    private FinancialTransaction payment;

    public PaymentResourceData(FinancialTransaction payment) {
        this.payment = payment;
    }

    @JsonProperty("type")
    public String getType() {
        return StringUtils.capitalize(payment.getTransactionType().name().toLowerCase());
    }

    @JsonProperty("id")
    public String getId() {
        return payment.getId().toString();
    }

    @JsonProperty("organisation_id")
    public String getOrganisationId() {
        return payment.getOrganisationId().toString();
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return payment.getVersion();
    }

    @JsonProperty("attributes")
    public PaymentAttributesResource getPaymentAttributes() {
        FinancialTransactionAttributes attributes = payment.getAttributes();
        if (attributes == null) {
            return null;
        }
        return new PaymentAttributesResource(attributes);
    }
}
