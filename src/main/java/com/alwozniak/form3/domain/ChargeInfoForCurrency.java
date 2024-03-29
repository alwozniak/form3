package com.alwozniak.form3.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "charge_info_for_currency")
public class ChargeInfoForCurrency {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private Double amount;

    @OneToOne(mappedBy = "receiverCharges")
    private ChargesInformation receiverChargesInformation;

    @ManyToOne
    @JoinColumn(name = "charges_information_id")
    private ChargesInformation senderChargesInformation;

    //
    // Constructors.
    //

    public ChargeInfoForCurrency(String currency, Double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public ChargeInfoForCurrency() {
        // For Hibernate.
    }

    //
    // Field accessors.
    //

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
