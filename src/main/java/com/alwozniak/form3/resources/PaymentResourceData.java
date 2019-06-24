package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransaction.FinancialTransactionType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class PaymentResourceData {

    private FinancialTransactionType transactionType;
    private UUID id;
    private UUID organisationId;
    private Integer version;
    private FinancialTransactionAttributes attributes;

    public PaymentResourceData(FinancialTransaction payment) {
        this.transactionType = payment.getTransactionType();
        this.organisationId = payment.getOrganisationId();
        this.id = payment.getId();
        this.version = payment.getVersion();
        this.attributes = payment.getAttributes();
    }

    @JsonProperty("type")
    public String getType() {
        return transactionType == null ? null : StringUtils.capitalize(transactionType.name().toLowerCase());
    }

    @JsonProperty("id")
    public String getId() {
        return id.toString();
    }

    @JsonProperty("organisation_id")
    public String getOrganisationId() {
        return organisationId == null ? null : organisationId.toString();
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("attributes")
    public PaymentAttributesResource getPaymentAttributes() {
        return attributes == null ? null : new PaymentAttributesResource(attributes);
    }
}
