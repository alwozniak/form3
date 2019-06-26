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

    public TransactionPartyResource() {
        // For Jackson.
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("account_name")
    public String getAccountName() {
        return accountName;
    }

    @JsonProperty("account_name")
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("account_number")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("account_number_code")
    public String getAccountNumberCodeString() {
        return accountNumberCode == null ? null : accountNumberCode.name();
    }

    public AccountNumberCode getAccountNumberCode() {
        return this.accountNumberCode;
    }

    @JsonProperty("account_number_code")
    public void setAccountNumberCode(String accountNumberCode) {
        this.accountNumberCode = AccountNumberCode.valueOf(accountNumberCode);
    }

    @JsonProperty("account_type")
    public Integer getAccountType() {
        return accountType;
    }

    @JsonProperty("account_type")
    public void setAccountType(int type) {
        this.accountType = type;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("bank_id")
    public String getBankId() {
        return bankId;
    }

    @JsonProperty("bank_id")
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @JsonProperty("bank_id_code")
    public String getBankIdCode() {
        return bankIdCode;
    }

    @JsonProperty("bank_id_code")
    public void setBankIdCode(String bankIdCode) {
        this.bankIdCode = bankIdCode;
    }
}

