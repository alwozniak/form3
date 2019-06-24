package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ChargesInformation;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentScheme;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentSubType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentType;
import com.alwozniak.form3.domain.ForeignExchangeInfo;
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
        return paymentScheme == null ? null : paymentScheme.name();
    }

    @JsonProperty("payment_type")
    public String getPaymentType() {
        PaymentType paymentType = attributes.getPaymentType();
        return paymentType == null ? null : StringUtils.capitalize(paymentType.name().toLowerCase());
    }

    @JsonProperty("processing_date")
    public String getProcessingDate() {
        Date processingDate = attributes.getProcessingDate();
        return processingDate == null ? null : DATE_FORMAT.format(processingDate);
    }

    @JsonProperty("reference")
    public String getReference() {
        return attributes.getReference();
    }

    @JsonProperty("scheme_payment_type")
    public String getSchemePaymentType() {
        SchemePaymentType schemePaymentType = attributes.getSchemePaymentType();
        return schemePaymentType == null ? null : toCamelCase(schemePaymentType.name());
    }

    @JsonProperty("scheme_payment_sub_type")
    public String getSchemePaymentSubType() {
        SchemePaymentSubType schemePaymentSubType = attributes.getSchemePaymentSubType();
        return schemePaymentSubType == null ? null : toCamelCase(schemePaymentSubType.name());
    }

    @JsonProperty("beneficiary_party")
    public TransactionPartyResource getBeneficiaryParty() {
        TransactionParty beneficiaryParty = attributes.getBeneficiaryParty();
        return beneficiaryParty == null ? null : new TransactionPartyResource(beneficiaryParty);
    }

    @JsonProperty("debtor_party")
    public TransactionPartyResource getDebtorParty() {
        TransactionParty debtorParty = attributes.getDebtorParty();
        return debtorParty == null ? null : new TransactionPartyResource(debtorParty);
    }

    @JsonProperty("fx")
    public ForeignExchangeInfoResource getFx() {
        ForeignExchangeInfo foreignExchangeInfo = attributes.getForeignExchangeInfo();
        return foreignExchangeInfo == null ? null : new ForeignExchangeInfoResource(foreignExchangeInfo);
    }

    @JsonProperty("charges_information")
    public ChargesInformationResource getChargesInformation() {
        ChargesInformation chargesInformation = attributes.getChargesInformation();
        return chargesInformation == null ? null : new ChargesInformationResource(chargesInformation);
    }

    private static String toCamelCase(String name) {
        return CaseUtils.toCamelCase(name.toLowerCase(), true, '_');
    }
}
