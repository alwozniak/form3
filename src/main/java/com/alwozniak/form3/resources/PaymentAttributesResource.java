package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import java.text.SimpleDateFormat;

public class PaymentAttributesResource {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private FinancialTransactionAttributes attributes;

    public PaymentAttributesResource(FinancialTransactionAttributes attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return attributes.getAmount();
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return attributes.getCurrency();
    }

    @JsonProperty("end_to_end_reference")
    public String getEndToEndReference() {
        return attributes.getEndToEndReference();
    }

    @JsonProperty("numeric_reference")
    public String getNumericReference() {
        return attributes.getNumericReference();
    }

    @JsonProperty("payment_id")
    public String getPaymentId() {
        return attributes.getPaymentId();
    }

    @JsonProperty("payment_purpose")
    public String getPaymentPurpose() {
        return attributes.getPaymentPurpose();
    }

    @JsonProperty("payment_scheme")
    public String getPaymentScheme() {
        return attributes.getPaymentScheme().name();
    }

    @JsonProperty("payment_type")
    public String getPaymentType() {
        return StringUtils.capitalize(attributes.getPaymentType().name().toLowerCase());
    }

    @JsonProperty("processing_date")
    public String getProcessingDate() {
        return DATE_FORMAT.format(attributes.getProcessingDate());
    }

    @JsonProperty("reference")
    public String getReference() {
        return attributes.getReference();
    }

    @JsonProperty("scheme_payment_type")
    public String getSchemePaymentType() {
        return toCamelCase(attributes.getSchemePaymentType().name());
    }

    @JsonProperty("scheme_payment_sub_type")
    public String getSchemePaymentSubType() {
        return toCamelCase(attributes.getSchemePaymentSubType().name());
    }

    private static String toCamelCase(String name) {
        return CaseUtils.toCamelCase(name.toLowerCase(), true, '_');
    }
}
