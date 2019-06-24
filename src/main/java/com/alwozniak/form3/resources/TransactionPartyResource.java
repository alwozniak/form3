package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import com.alwozniak.form3.domain.TransactionParty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionPartyResource {

    private String name;
    private String accountName;
    private String accountNumber;
    private AccountNumberCode accountNumberCode;
    private Integer accountType;
    private String bankIdCode;
    private String address;
    private String bankId;

    public TransactionPartyResource(TransactionParty transactionParty) {
        this.name = transactionParty.getName();
        this.accountName = transactionParty.getAccountName();
        this.accountNumber = transactionParty.getAccountNumber();
        this.accountNumberCode = transactionParty.getAccountNumberCode();
        this.accountType = transactionParty.getAccountType();
        this.bankIdCode = transactionParty.getBankIdCode();
        this.address = transactionParty.getAddress();
        this.bankId = transactionParty.getBankId();
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("account_name")
    public String getAccountName() {
        return accountName;
    }

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("account_number_code")
    public String getAccountNumberCode() {
        return accountNumberCode == null ? null : accountNumberCode.name();
    }

    @JsonProperty("account_type")
    public Integer getAccountType() {
        return accountType;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
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

