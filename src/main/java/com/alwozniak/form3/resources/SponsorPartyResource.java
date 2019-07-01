package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.TransactionParty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SponsorPartyResource {

    private final String bankIdCode;
    private String bankId;
    private String accountNumber;

    public SponsorPartyResource(TransactionParty sponsorParty) {
        bankIdCode = sponsorParty.getBankIdCode();
        bankId = sponsorParty.getBankId();
        accountNumber = sponsorParty.getAccountNumber();
    }

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("bank_id")
    public String getBankId() {
        return bankId;
    }

    @JsonProperty("bank_id_code")
    public String getBankIdCode() {
        return bankIdCode;
    }
}
