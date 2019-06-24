package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentScheme;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentSubType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentType;
import com.alwozniak.form3.domain.TransactionParty;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        PaymentScheme paymentScheme = attributes.getPaymentScheme();
        if (paymentScheme == null) {
            return null;
        }
        return paymentScheme.name();
    }

    @JsonProperty("payment_type")
    public String getPaymentType() {
        PaymentType paymentType = attributes.getPaymentType();
        if (paymentType == null) {
             return null;
        }
        return StringUtils.capitalize(paymentType.name().toLowerCase());
    }

    @JsonProperty("processing_date")
    public String getProcessingDate() {
        Date processingDate = attributes.getProcessingDate();
        if (processingDate == null) {
            return null;
        }
        return DATE_FORMAT.format(processingDate);
    }

    @JsonProperty("reference")
    public String getReference() {
        return attributes.getReference();
    }

    @JsonProperty("scheme_payment_type")
    public String getSchemePaymentType() {
        SchemePaymentType schemePaymentType = attributes.getSchemePaymentType();
        if (schemePaymentType == null) {
            return null;
        }
        return toCamelCase(schemePaymentType.name());
    }

    @JsonProperty("scheme_payment_sub_type")
    public String getSchemePaymentSubType() {
        SchemePaymentSubType schemePaymentSubType = attributes.getSchemePaymentSubType();
        if (schemePaymentSubType == null) {
            return null;
        }
        return toCamelCase(schemePaymentSubType.name());
    }

    @JsonProperty("beneficiary_party")
    public TransactionPartyResource getBeneficiaryParty() {
        TransactionParty beneficiaryParty = attributes.getBeneficiaryParty();
        if (beneficiaryParty == null) {
            return null;
        }
        return new TransactionPartyResource(beneficiaryParty);
    }

    private static String toCamelCase(String name) {
        return CaseUtils.toCamelCase(name.toLowerCase(), true, '_');
    }
}
