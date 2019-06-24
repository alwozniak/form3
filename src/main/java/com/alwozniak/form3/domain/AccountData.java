package com.alwozniak.form3.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "account_data")
public class AccountData {

    public enum AccountNumberCode {
        BBAN,
        IBAN
    }

    public static class Builder {

        private String accountName;
        private AccountNumberCode accountNumberCode;
        private String accountNumber;
        private int accountType;

        public Builder(String accountName) {
            this.accountName = accountName;
        }

        public Builder withBbanNumber(String accountNumber) {
            this.accountNumberCode = AccountNumberCode.BBAN;
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder withType(int accountType) {
            this.accountType = accountType;
            return this;
        }

        public AccountData build() {
            return new AccountData(accountName, accountNumber, accountNumberCode, accountType);
        }

        public Builder withIbanNumber(String accountNumber) {
            this.accountNumberCode = AccountNumberCode.IBAN;
            this.accountNumber = accountNumber;
            return this;
        }
    }

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_number_type")
    private AccountNumberCode accountNumberCode;

    @Column(name = "account_type")
    private Integer accountType;

    @OneToOne
    private TransactionParty transactionParty;

    //
    // Constructors, factory methods, builder methods.
    //

    public static Builder builder(String accountName) {
        return new Builder(accountName);
    }

    private AccountData(String accountName, String accountNumber, AccountNumberCode accountNumberCode, int accountType) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountNumberCode = accountNumberCode;
        this.accountType = accountType;
    }

    public AccountData() {
        // For Hibernate.
    }

    public AccountData(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    //
    // Field accessors.
    //

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountNumberCode getAccountNumberCode() {
        return accountNumberCode;
    }

    public Integer getAccountType() {
        return accountType;
    }
}
