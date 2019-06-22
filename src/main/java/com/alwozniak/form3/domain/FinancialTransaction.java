package com.alwozniak.form3.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

    public static final int DEFAULT_VERSION = 0;

    public enum FinancialTransactionType {
        PAYMENT
    }

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private FinancialTransactionType transactionType;

    @Column(name = "version")
    private int version;

    @Column(name = "organisation_id")
    private UUID organisationId;

    @OneToOne(cascade = CascadeType.ALL)
    private FinancialTransactionAttributes attributes;

    //
    // Constructors and factory methods.
    //

    public static FinancialTransaction newPayment(UUID organisationId) {
        return new FinancialTransaction(FinancialTransactionType.PAYMENT, DEFAULT_VERSION, organisationId);
    }

    public FinancialTransaction() {
        // For Hibernate.
    }

    private FinancialTransaction(FinancialTransactionType payment, int defaultVersion, UUID organisationId) {
        transactionType = payment;
        version = defaultVersion;
        this.organisationId = organisationId;
    }

    //
    // Field accessors.
    //

    public UUID getId() {
        return id;
    }

    public FinancialTransactionType getTransactionType() {
        return transactionType;
    }

    public int getVersion() {
        return version;
    }

    public UUID getOrganisationId() {
        return organisationId;
    }

    public FinancialTransactionAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(FinancialTransactionAttributes attributes) {
        this.attributes = attributes;
    }
}
