package com.alwozniak.form3.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "charges_information")
public class ChargesInformation {

    public void setFinancialTransactionAttributes(FinancialTransactionAttributes financialTransactionAttributes) {
        this.financialTransactionAttributes = financialTransactionAttributes;
    }

    public enum ChargeType {
        SHARED
    }

    public static class Builder {

        private final ChargeType chargeType;
        private ChargeInfoForCurrency receiverCharges;
        private List<ChargeInfoForCurrency> senderCharges = new ArrayList<>();

        public Builder(ChargeType chargeType) {
            this.chargeType = chargeType;
        }

        public Builder withReceiverCharge(Double amount, String currency) {
            this.receiverCharges = new ChargeInfoForCurrency(currency, amount);
            return this;
        }

        public Builder withSenderCharge(Double amount, String currency) {
            this.senderCharges.add(new ChargeInfoForCurrency(currency, amount));
            return this;
        }

        public ChargesInformation build() {
            return new ChargesInformation(chargeType, receiverCharges, senderCharges);
        }
    }

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_charge_id")
    private ChargeInfoForCurrency receiverCharges;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChargeInfoForCurrency> senderCharges;

    @OneToOne(mappedBy = "chargesInformation")
    private FinancialTransactionAttributes financialTransactionAttributes;

    //
    // Constructors and factory methods.
    //

    public static Builder builder(ChargeType chargeType) {
        return new Builder(chargeType);
    }

    private ChargesInformation(ChargeType chargeType, ChargeInfoForCurrency receiverCharges,
                              List<ChargeInfoForCurrency> senderCharges) {

        this.chargeType = chargeType;
        this.receiverCharges = receiverCharges;
        this.senderCharges = senderCharges;
    }

    public ChargesInformation() {
        // For Hibernate.
    }

    //
    // Field accessors and other access methods.
    //

    public ChargeType getBearerCode() {
        return chargeType;
    }

    public Double getReceiverChargesAmount() {
        return receiverCharges.getAmount();
    }

    public String getReceiverChargesCurrency() {
        return receiverCharges.getCurrency();
    }

    public List<ChargeInfoForCurrency> getSenderCharges() {
        return senderCharges;
    }
}
