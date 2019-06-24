package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ChargeInfoForCurrency;
import com.alwozniak.form3.domain.ChargesInformation;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ChargesInformationResource {
    private ChargesInformation chargesInformation;

    public ChargesInformationResource(ChargesInformation chargesInformation) {
        this.chargesInformation = chargesInformation;
    }

    @JsonProperty("bearer_code")
    public String getBearerCode() {
        ChargesInformation.ChargeType bearerCode = chargesInformation.getBearerCode();
        return bearerCode == null ? null : bearerCode.name().substring(0, 4);
    }

    @JsonProperty("receiver_charges_amount")
    public String getReceiverChargesAmount() {
        Double receiverChargesAmount = chargesInformation.getReceiverChargesAmount();
        return receiverChargesAmount == null ? null : String.format("%.2f", receiverChargesAmount);
    }

    @JsonProperty("receiver_charges_currency")
    public String getReceiverChargesCurrency() {
        return chargesInformation.getReceiverChargesCurrency();
    }

    @JsonProperty("sender_charges")
    public List<ChargeInfoForCurrencyResource> getSenderCharges() {
        List<ChargeInfoForCurrency> senderCharges = chargesInformation.getSenderCharges();
        return senderCharges == null ? null :
                senderCharges.stream()
                        .map(ChargeInfoForCurrencyResource::new)
                        .collect(Collectors.toList());
    }
}
