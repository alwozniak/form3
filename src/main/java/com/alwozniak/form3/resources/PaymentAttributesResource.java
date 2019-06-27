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

import java.text.ParseException;
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
    private TransactionPartyResource beneficiaryPartyResource;

    private TransactionParty debtorParty;
    private TransactionPartyResource debtorPartyResource;

    private ForeignExchangeInfo foreignExchangeInfo;
    private ForeignExchangeInfoResource foreignExchangeInfoResource;

    private ChargesInformation chargesInformation;
    private ChargesInformationResource chargesInformationResource;

    private TransactionParty sponsorParty;
    private TransactionPartyResource sponsorPartyResource;

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

    public PaymentAttributesResource() {
        // For Jackson.
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmountFromString(String amountString) {
        this.amount = Double.valueOf(amountString);
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("end_to_end_reference")
    public String getEndToEndReference() {
        return endToEndReference;
    }

    @JsonProperty("end_to_end_reference")
    public void setEndToEndReference(String endToEndReference) {
        this.endToEndReference = endToEndReference;
    }

    @JsonProperty("numeric_reference")
    public String getNumericReference() {
        return numericReference;
    }

    @JsonProperty("numeric_reference")
    public void setNumericReference(String numericReference) {
        this.numericReference = numericReference;
    }

    @JsonProperty("payment_id")
    public String getPaymentId() {
        return paymentId;
    }

    @JsonProperty("payment_id")
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @JsonProperty("payment_purpose")
    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    @JsonProperty("payment_purpose")
    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }

    public PaymentScheme getPaymentScheme() {
        return this.paymentScheme;
    }

    @JsonProperty("payment_scheme")
    public String getPaymentSchemeString() {
        return paymentScheme == null ? null : paymentScheme.name();
    }

    @JsonProperty("payment_scheme")
    public void setPaymentSchemeFromString(String paymentSchemeString) {
        this.paymentScheme = PaymentScheme.valueOf(paymentSchemeString);
    }

    @JsonProperty("payment_type")
    public String getPaymentTypeString() {
        return paymentType == null ? null : StringUtils.capitalize(paymentType.name().toLowerCase());
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    @JsonProperty("payment_type")
    public void setPaymentTypeFromString(String paymentTypeString) {
        this.paymentType = PaymentType.valueOf(paymentTypeString.toUpperCase());
    }

    @JsonProperty("processing_date")
    public String getProcessingDateString() {
        return processingDate == null ? null : DATE_FORMAT.format(processingDate);
    }

    public Date getProcessingDate() {
        return this.processingDate;
    }

    @JsonProperty("processing_date")
    public void setProcessingDateFromString(String processingDateString) throws ParseException {
        this.processingDate = DATE_FORMAT.parse(processingDateString);
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("scheme_payment_type")
    public String getSchemePaymentTypeString() {
        return schemePaymentType == null ? null : toCamelCase(schemePaymentType.name());
    }

    public SchemePaymentType getSchemePaymentType() {
        return this.schemePaymentType;
    }

    @JsonProperty("scheme_payment_type")
    public void setSchemePaymentTypeFromString(String schemePaymentTypeString) {
        this.schemePaymentType = SchemePaymentType.valueOf(toUpperSnakeCase(schemePaymentTypeString));
    }

    @JsonProperty("scheme_payment_sub_type")
    public String getSchemePaymentSubTypeString() {
        return schemePaymentSubType == null ? null : toCamelCase(schemePaymentSubType.name());
    }

    public SchemePaymentSubType getSchemePaymentSubType() {
        return this.schemePaymentSubType;
    }

    @JsonProperty("scheme_payment_sub_type")
    public void setSchemePaymentSubTypeFromString(String schemePaymentSubTypeString) {
        this.schemePaymentSubType = SchemePaymentSubType.valueOf(toUpperSnakeCase(schemePaymentSubTypeString));
    }

    @JsonProperty("beneficiary_party")
    public TransactionPartyResource createBeneficiaryPartyResource() {
        return beneficiaryParty == null ? null : new TransactionPartyResource(beneficiaryParty);
    }

    public TransactionPartyResource getBeneficiaryPartyResource() {
        return this.beneficiaryPartyResource;
    }

    @JsonProperty("beneficiary_party")
    public void setBeneficiaryPartyResource(TransactionPartyResource beneficiaryPartyResource) {
        this.beneficiaryPartyResource = beneficiaryPartyResource;
    }

    @JsonProperty("debtor_party")
    public TransactionPartyResource createDebtorPartyResource() {
        return debtorParty == null ? null : new TransactionPartyResource(debtorParty);
    }

    @JsonProperty("debtor_party")
    public void setDebtorPartyResource(TransactionPartyResource debtorPartyResource) {
        this.debtorPartyResource = debtorPartyResource;
    }

    public TransactionPartyResource getDebtorPartyResource() {
        return this.debtorPartyResource;
    }

    @JsonProperty("fx")
    public ForeignExchangeInfoResource createFxResource() {
        return foreignExchangeInfo == null ? null : new ForeignExchangeInfoResource(foreignExchangeInfo);
    }

    @JsonProperty("fx")
    public void setFxResource(ForeignExchangeInfoResource foreignExchangeInfoResource) {
        this.foreignExchangeInfoResource = foreignExchangeInfoResource;
    }

    @JsonProperty("charges_information")
    public ChargesInformationResource createChargesInformationResource() {
        return chargesInformation == null ? null : new ChargesInformationResource(chargesInformation);
    }

    public ChargesInformationResource getChargesInformationResource() {
        return this.chargesInformationResource;
    }

    @JsonProperty("charges_information")
    public void setChargesInformationResource(ChargesInformationResource chargesInformationResource) {
        this.chargesInformationResource = chargesInformationResource;
    }

    @JsonProperty("sponsor_party")
    public SponsorPartyResource createSponsorPartyResource() {
        return sponsorParty == null ? null : new SponsorPartyResource(sponsorParty);
    }

    @JsonProperty("sponsor_party")
    public void setSponsorPartyResource(TransactionPartyResource sponsorPartyResource) {
        this.sponsorPartyResource = sponsorPartyResource;
    }

    public TransactionPartyResource getSponsorPartyResource() {
        return this.sponsorPartyResource;
    }

    private static String toCamelCase(String name) {
        return CaseUtils.toCamelCase(name.toLowerCase(), true, '_');
    }

    private static String toUpperSnakeCase(String text) {
        return toSnakeCase(text).toUpperCase();
    }

    private static String toSnakeCase(String text) {
        return text.replaceAll("([^_A-Z])([A-Z])", "$1_$2");
    }

    public ForeignExchangeInfoResource getForeignExchangeInfoResource() {
        return this.foreignExchangeInfoResource;
    }
}
