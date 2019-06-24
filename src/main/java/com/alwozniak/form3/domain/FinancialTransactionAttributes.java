package com.alwozniak.form3.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "financial_transaction_attributes")
public class FinancialTransactionAttributes {

    public static class Builder {

        private final FinancialTransaction transaction;
        private Double amount;
        private String currency;
        private String endToEndReference;
        private String numericReference;
        private String paymentId;
        private String paymentPurpose;
        private PaymentType paymentType;
        private PaymentScheme paymentScheme;
        private Date processingDate;
        private String reference;
        private SchemePaymentType schemePaymentType;
        private SchemePaymentSubType schemePaymentSubType;
        private TransactionParty beneficiaryParty;
        private TransactionParty debtorParty;
        private ForeignExchangeInfo foreignExchangeInfo;
        private ChargesInformation chargesInformation;

        public Builder(FinancialTransaction transaction) {
            this.transaction = transaction;
        }

        public FinancialTransactionAttributes build() {
            return new FinancialTransactionAttributes(transaction, amount, currency, endToEndReference, numericReference,
                    paymentId, paymentPurpose, paymentType, paymentScheme, processingDate, reference, schemePaymentType,
                    schemePaymentSubType, beneficiaryParty, debtorParty, foreignExchangeInfo, chargesInformation);
        }

        public Builder withAmountInCurrency(Double amount, String currency) {
            this.amount = amount;
            this.currency = currency;
            return this;
        }

        public Builder withEndToEndReference(String endToEndReference) {
            this.endToEndReference = endToEndReference;
            return this;
        }

        public Builder withNumericReference(String numericReference) {
            this.numericReference = numericReference;
            return this;
        }

        public Builder withPaymentData(String paymentId, String paymentPurpose, PaymentType paymentType) {
            this.paymentId = paymentId;
            this.paymentPurpose = paymentPurpose;
            this.paymentType = paymentType;
            return this;
        }

        public Builder withProcessingDate(Date processingDate) {
            this.processingDate = processingDate;
            return this;
        }

        public Builder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder withPaymentSchemeData(PaymentScheme paymentScheme, SchemePaymentType schemePaymentType,
                                             SchemePaymentSubType schemePaymentSubType) {
            this.paymentScheme = paymentScheme;
            this.schemePaymentType = schemePaymentType;
            this.schemePaymentSubType = schemePaymentSubType;
            return this;
        }

        public Builder withBeneficiary(TransactionParty beneficiary) {
            this.beneficiaryParty = beneficiary;
            return this;
        }

        public Builder withDebtor(TransactionParty debtor) {
            this.debtorParty = debtor;
            return this;
        }

        public Builder withForeignExchangeInfo(ForeignExchangeInfo foreignExchangeInfo) {
            this.foreignExchangeInfo = foreignExchangeInfo;
            return this;
        }

        public Builder withChargesInformation(ChargesInformation chargesInformation) {
            this.chargesInformation = chargesInformation;
            return this;
        }
    }

    public enum PaymentScheme {
        FPS
    }

    public enum PaymentType {
        CREDIT
    }

    public enum SchemePaymentType {
        IMMEDIATE_PAYMENT
    }

    public enum SchemePaymentSubType {
        INTERNET_BANKING
    }

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private FinancialTransaction transaction;

    @Column(name = "amount")
    private Double amount; // TODO change type to BigInteger

    @Column(name = "currency")
    private String currency;

    @Column(name = "end_to_end_reference")
    private String endToEndReference;

    @Column(name = "numeric_reference")
    private String numericReference;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payment_purpose")
    private String paymentPurpose;

    @Column(name = "payment_scheme")
    @Enumerated(EnumType.STRING)
    private PaymentScheme paymentScheme;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "processing_date")
    @Temporal(TemporalType.DATE)
    private Date processingDate;

    @Column(name = "reference")
    private String reference;

    @Column(name = "scheme_payment_sub_type")
    @Enumerated(EnumType.STRING)
    private SchemePaymentSubType schemePaymentSubType;

    @Column(name = "scheme_payment_type")
    @Enumerated(EnumType.STRING)
    private SchemePaymentType schemePaymentType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "beneficiary_party_id")
    private TransactionParty beneficiaryParty;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debtor_party_id")
    private TransactionParty debtorParty;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "foreign_exchange_info_id",
            foreignKey = @ForeignKey(name = "fk_foreignExchangeInfo_financialTransactionAttributes"))
    private ForeignExchangeInfo foreignExchangeInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financial_transaction_attributes_id",
            foreignKey = @ForeignKey(name = "fk_chargesInformation_financial_transaction_attributes"))
    private ChargesInformation chargesInformation;

    //
    // Constructors, factory methods and builders.
    //

    public static FinancialTransactionAttributes.Builder builder(FinancialTransaction transaction) {
        return new Builder(transaction);
    }

    public FinancialTransactionAttributes() {
        // For Hibernate.
    }

    private FinancialTransactionAttributes(FinancialTransaction transaction, Double amount,
                                           String currency,
                                           String endToEndReference,
                                           String numericReference,
                                           String paymentId,
                                           String paymentPurpose,
                                           PaymentType paymentType,
                                           PaymentScheme paymentScheme,
                                           Date processingDate,
                                           String reference,
                                           SchemePaymentType schemePaymentType,
                                           SchemePaymentSubType schemePaymentSubType,
                                           TransactionParty beneficiaryParty,
                                           TransactionParty debtorParty,
                                           ForeignExchangeInfo foreignExchangeInfo,
                                           ChargesInformation chargesInformation) {
        this.transaction = transaction;
        this.amount = amount;
        this.currency = currency;
        this.endToEndReference = endToEndReference;
        this.numericReference = numericReference;
        this.paymentId = paymentId;
        this.paymentPurpose = paymentPurpose;
        this.paymentType = paymentType;
        this.paymentScheme = paymentScheme;
        this.processingDate = processingDate;
        this.reference = reference;
        this.schemePaymentType = schemePaymentType;
        this.schemePaymentSubType = schemePaymentSubType;
        this.beneficiaryParty = beneficiaryParty;
        this.debtorParty = debtorParty;
        this.foreignExchangeInfo = foreignExchangeInfo;
        this.chargesInformation = chargesInformation;
    }

    //
    // Field accessors.
    //

    public UUID getId() {
        return id;
    }

    public FinancialTransaction getTransaction() {
        return transaction;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getEndToEndReference() {
        return endToEndReference;
    }

    public String getNumericReference() {
        return numericReference;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public PaymentScheme getPaymentScheme() {
        return paymentScheme;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public Date getProcessingDate() {
        return processingDate;
    }

    public String getReference() {
        return reference;
    }

    public SchemePaymentType getSchemePaymentType() {
        return schemePaymentType;
    }

    public SchemePaymentSubType getSchemePaymentSubType() {
        return schemePaymentSubType;
    }

    public TransactionParty getBeneficiaryParty() {
        return beneficiaryParty;
    }

    public TransactionParty getDebtorParty() {
        return debtorParty;
    }

    public ForeignExchangeInfo getForeignExchangeInfo() {
        return foreignExchangeInfo;
    }

    public ChargesInformation getChargesInformation() {
        return chargesInformation;
    }
}
