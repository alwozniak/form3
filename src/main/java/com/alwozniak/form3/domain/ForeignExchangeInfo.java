package com.alwozniak.form3.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "foreign_exchange_info")
public class ForeignExchangeInfo {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "contract_reference")
    private String contractReference;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "original_amount")
    private Double originalAmount;

    @Column(name = "original_currency")
    private String originalCurrency;

    @OneToOne(mappedBy = "foreignExchangeInfo")
    private FinancialTransactionAttributes financialTransactionAttributes;

    //
    // Constructors.
    //

    public ForeignExchangeInfo(String contractReference, Double exchangeRate, Double originalAmount,
                               String originalCurrency) {
        this.contractReference = contractReference;
        this.exchangeRate = exchangeRate;
        this.originalAmount = originalAmount;
        this.originalCurrency = originalCurrency;
    }

    public ForeignExchangeInfo() {
        // For Hibernate.
    }

    //
    // Field accessors.
    //

    public void setFinancialTransactionAttributes(FinancialTransactionAttributes financialTransactionAttributes) {
        this.financialTransactionAttributes = financialTransactionAttributes;
    }

    public String getContractReference() {
        return contractReference;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public Double getOriginalAmount() {
        return originalAmount;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void updateFields(String contractReference, Double exchangeRate, String originalCurrency, Double originalAmount) {
        if (contractReference != null) {
            this.contractReference = contractReference;
        }
        if (exchangeRate != null) {
            this.exchangeRate = exchangeRate;
        }
        if (originalCurrency != null) {
            this.originalCurrency = originalCurrency;
        }
        if (originalAmount != null) {
            this.originalAmount = originalAmount;
        }
    }
}
