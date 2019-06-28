package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransaction.FinancialTransactionType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class PaymentResourceData {

    private FinancialTransactionType transactionType;
    private UUID id;
    private UUID organisationId;
    private Integer version;
    private FinancialTransactionAttributes attributes;
    private PaymentAttributesResource paymentAttributesResource;

    public PaymentResourceData(FinancialTransaction payment) {
        this.transactionType = payment.getTransactionType();
        this.organisationId = payment.getOrganisationId();
        this.id = payment.getId();
        this.version = payment.getVersion();
        this.attributes = payment.getAttributes();
    }

    public PaymentResourceData() {
        // For Jackson.
    }

    @JsonProperty("type")
    public String getTypeString() {
        return transactionType == null ? null : StringUtils.capitalize(transactionType.name().toLowerCase());
    }

    @JsonProperty("type")
    public void setTypeFromString(String transactionTypeString) {
        this.transactionType = FinancialTransactionType.valueOf(transactionTypeString.toUpperCase());
    }

    @JsonIgnore
    public FinancialTransactionType getTransactionType() {
        return this.transactionType;
    }

    @JsonProperty("id")
    public String getId() {
        return id.toString();
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(int version) {
        this.version = version;
    }

    @JsonProperty("attributes")
    public PaymentAttributesResource createPaymentAttributesResource() {
        return attributes == null ? null : new PaymentAttributesResource(attributes);
    }

    @JsonProperty("attributes")
    public void setPaymentAttributesResource(PaymentAttributesResource paymentAttributesResource) {
        this.paymentAttributesResource = paymentAttributesResource;
    }

    @JsonIgnore
    public PaymentAttributesResource getPaymentAttributesResource() {
        return this.paymentAttributesResource;
    }

    @JsonProperty("organisation_id")
    public String getOrganisationIdAsString() {
        return organisationId == null ? null : organisationId.toString();
    }

    @JsonProperty("organisation_id")
    public void setOrganisationIdFromString(String organisationIdString) {
        this.organisationId = UUID.fromString(organisationIdString);
    }

    @JsonIgnore
    public UUID getOrganisationId() {
        return organisationId;
    }
}
