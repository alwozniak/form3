package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.TransactionParty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SponsorPartyResource {
    private TransactionParty sponsorParty;

    public SponsorPartyResource(TransactionParty sponsorParty) {
        this.sponsorParty = sponsorParty;
    }

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return sponsorParty.getAccountNumber();
    }

    @JsonProperty("bank_id")
    public String getBankId() {
        return sponsorParty.getBankId();
    }

    @JsonProperty("bank_id_code")
    public String getBankIdCode() {
        return sponsorParty.getBankIdCode();
    }
}
