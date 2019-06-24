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

    private Double amount;
    private String currency;
    private String endToEndReference;
    private String numericReference;
    private String paymentId;
    private PaymentScheme paymentScheme;
    private String paymentPurpose;
    private Date processingDate;
    private String reference;
    private PaymentType paymentType;
    private SchemePaymentType schemePaymentType;
    private SchemePaymentSubType schemePaymentSubType;
    private TransactionParty beneficiaryParty;
    private TransactionParty debtorParty;
    private ForeignExchangeInfo foreignExchangeInfo;
    private ChargesInformation chargesInformation;
    private TransactionParty sponsorParty;

    public PaymentAttributesResource(FinancialTransactionAttributes attributes) {
        this.sponsorParty = attributes.getSponsorParty();
        this.amount = attributes.getAmount();
        this.currency = attributes.getCurrency();
        this.endToEndReference = attributes.getEndToEndReference();
        this.numericReference = attributes.getNumericReference();
        this.paymentId = attributes.getPaymentId();
        this.paymentScheme = attributes.getPaymentScheme();
        this.paymentPurpose = attributes.getPaymentPurpose();
        this.paymentType = attributes.getPaymentType();
        this.processingDate = attributes.getProcessingDate();
        this.reference = attributes.getReference();
        this.schemePaymentType = attributes.getSchemePaymentType();
        this.schemePaymentSubType = attributes.getSchemePaymentSubType();
        this.beneficiaryParty = attributes.getBeneficiaryParty();
        this.debtorParty = attributes.getDebtorParty();
        this.foreignExchangeInfo = attributes.getForeignExchangeInfo();
        this.chargesInformation = attributes.getChargesInformation();
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("end_to_end_reference")
    public String getEndToEndReference() {
        return endToEndReference;
    }

    @JsonProperty("numeric_reference")
    public String getNumericReference() {
        return numericReference;
    }

    @JsonProperty("payment_id")
    public String getPaymentId() {
        return paymentId;
    }

    @JsonProperty("payment_purpose")
    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    @JsonProperty("payment_scheme")
    public String getPaymentScheme() {
        return paymentScheme == null ? null : paymentScheme.name();
    }

    @JsonProperty("payment_type")
    public String getPaymentType() {
        return paymentType == null ? null : StringUtils.capitalize(paymentType.name().toLowerCase());
    }

    @JsonProperty("processing_date")
    public String getProcessingDate() {
        return processingDate == null ? null : DATE_FORMAT.format(processingDate);
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("scheme_payment_type")
    public String getSchemePaymentType() {
        return schemePaymentType == null ? null : toCamelCase(schemePaymentType.name());
    }

    @JsonProperty("scheme_payment_sub_type")
    public String getSchemePaymentSubType() {
        return schemePaymentSubType == null ? null : toCamelCase(schemePaymentSubType.name());
    }

    @JsonProperty("beneficiary_party")
    public TransactionPartyResource getBeneficiaryParty() {
        return beneficiaryParty == null ? null : new TransactionPartyResource(beneficiaryParty);
    }

    @JsonProperty("debtor_party")
    public TransactionPartyResource getDebtorParty() {
        return debtorParty == null ? null : new TransactionPartyResource(debtorParty);
    }

    @JsonProperty("fx")
    public ForeignExchangeInfoResource getFx() {
        return foreignExchangeInfo == null ? null : new ForeignExchangeInfoResource(foreignExchangeInfo);
    }

    @JsonProperty("charges_information")
    public ChargesInformationResource getChargesInformation() {
        return chargesInformation == null ? null : new ChargesInformationResource(chargesInformation);
    }

    @JsonProperty("sponsor_party")
    public SponsorPartyResource getSponsorPartyResource() {
        return sponsorParty == null ? null : new SponsorPartyResource(sponsorParty);
    }

    private static String toCamelCase(String name) {
        return CaseUtils.toCamelCase(name.toLowerCase(), true, '_');
    }
}
