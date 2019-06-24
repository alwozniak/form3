package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ChargeInfoForCurrency;
import com.alwozniak.form3.domain.ChargesInformation;
import com.alwozniak.form3.domain.ChargesInformation.ChargeType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ChargesInformationResource {

    private ChargeType bearerCode;
    private Double receiverChargesAmount;
    private String receiverChargesCurrency;
    private List<ChargeInfoForCurrency> senderCharges;

    public ChargesInformationResource(ChargesInformation chargesInformation) {
        this.bearerCode = chargesInformation.getBearerCode();
        this.receiverChargesAmount = chargesInformation.getReceiverChargesAmount();
        this.receiverChargesCurrency = chargesInformation.getReceiverChargesCurrency();
        this.senderCharges = chargesInformation.getSenderCharges();
    }

    @JsonProperty("bearer_code")
    public String getBearerCode() {
        return bearerCode == null ? null : bearerCode.name().substring(0, 4);
    }

    @JsonProperty("receiver_charges_amount")
    public String getReceiverChargesAmount() {
        return receiverChargesAmount == null ? null : String.format("%.2f", receiverChargesAmount);
    }

    @JsonProperty("receiver_charges_currency")
    public String getReceiverChargesCurrency() {
        return receiverChargesCurrency;
    }

    @JsonProperty("sender_charges")
    public List<ChargeInfoForCurrencyResource> getSenderCharges() {
        return senderCharges == null ? null :
                senderCharges.stream()
                        .map(ChargeInfoForCurrencyResource::new)
                        .collect(Collectors.toList());
    }
}
