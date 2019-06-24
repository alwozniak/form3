package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import com.alwozniak.form3.domain.TransactionParty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionPartyResource {
    private TransactionParty transactionParty;

    public TransactionPartyResource(TransactionParty transactionParty) {
        this.transactionParty = transactionParty;
    }

    @JsonProperty("name")
    public String getName() {
        return transactionParty.getName();
    }

    @JsonProperty("account_name")
    public String getAccountName() {
        return transactionParty.getAccountName();
    }

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return transactionParty.getAccountNumber();
    }

    @JsonProperty("account_number_code")
    public String getAccountNumberCode() {
        AccountNumberCode accountNumberCode = transactionParty.getAccountNumberCode();
        if (accountNumberCode == null) {
            return null;
        }
        return accountNumberCode.name();
    }

    @JsonProperty("account_type")
    public Integer getAccountType() {
        return transactionParty.getAccountType();
    }

    @JsonProperty("address")
    public String getAddress() {
        return transactionParty.getAddress();
    }

    @JsonProperty("bank_id")
    public String getBankId() {
        return transactionParty.getBankId();
    }

    @JsonProperty("bank_id_code")
    public String getBankIdCode() {
        return transactionParty.getBankIdCode();
    }
}

