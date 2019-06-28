package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.ChargeInfoForCurrency;
import com.alwozniak.form3.domain.ChargesInformation;
import com.alwozniak.form3.domain.ChargesInformation.ChargeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ChargesInformationResource {

    private ChargeType bearerCode;
    private Double receiverChargesAmount;
    private String receiverChargesCurrency;

    private List<ChargeInfoForCurrency> senderCharges;
    private List<ChargeInfoForCurrencyResource> senderChargeResources;

    public ChargesInformationResource(ChargesInformation chargesInformation) {
        this.bearerCode = chargesInformation.getBearerCode();
        this.receiverChargesAmount = chargesInformation.getReceiverChargesAmount();
        this.receiverChargesCurrency = chargesInformation.getReceiverChargesCurrency();
        this.senderCharges = chargesInformation.getSenderCharges();
    }

    public ChargesInformationResource() {
        // For Jackson.
    }

    @JsonProperty("bearer_code")
    public String getBearerCodeString() {
        return bearerCode == null ? null : bearerCode.name().substring(0, 4);
    }

    @JsonProperty("bearer_code")
    public void setBearerCodeFromString(String bearerCode) {
        if (bearerCode.equals("SHAR")) {
            this.bearerCode = ChargeType.SHARED;
        }
    }

    @JsonIgnore
    public ChargeType getBearerCode() {
        return this.bearerCode;
    }

    public void setBearerCode(ChargeType bearerCode) {
        this.bearerCode = bearerCode;
    }

    @JsonProperty("receiver_charges_amount")
    public String getReceiverChargesAmountString() {
        return receiverChargesAmount == null ? null : String.format("%.2f", receiverChargesAmount);
    }

    @JsonProperty("receiver_charges_amount")
    public void setReceiverChargesAmountFromString(String receiverChargesAmountString) {
        this.receiverChargesAmount = Double.valueOf(receiverChargesAmountString);
    }

    public void setReceiverChargesAmount(Double receiverChargesAmount) {
        this.receiverChargesAmount = receiverChargesAmount;
    }

    @JsonIgnore
    public Double getReceiverChargesAmount() {
        return this.receiverChargesAmount;
    }

    @JsonProperty("receiver_charges_currency")
    public String getReceiverChargesCurrency() {
        return receiverChargesCurrency;
    }

    @JsonProperty("receiver_charges_currency")
    public void setReceiverChargesCurrency(String receiverChargeCurrency) {
        this.receiverChargesCurrency = receiverChargeCurrency;
    }

    @JsonProperty("sender_charges")
    public List<ChargeInfoForCurrencyResource> getSenderCharges() {
        return senderCharges == null ? null :
                senderCharges.stream()
                        .map(ChargeInfoForCurrencyResource::new)
                        .collect(Collectors.toList());
    }

    @JsonProperty("sender_charges")
    public void setSenderChargeResources(List<ChargeInfoForCurrencyResource> senderChargeResources) {
        this.senderChargeResources = senderChargeResources;
    }

    @JsonIgnore
    public List<ChargeInfoForCurrencyResource> getSenderChargeResources() {
        return this.senderChargeResources;
    }
}
