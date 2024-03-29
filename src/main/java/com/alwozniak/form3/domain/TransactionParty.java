package com.alwozniak.form3.domain;

import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "transaction_party")
public class TransactionParty {

    public static class Builder {

        private String name;
        private AccountData accountData;
        private String address;
        private String bankId;
        private String bankIdCode;

        public Builder withName(String ownersName) {
            this.name = ownersName;
            return this;
        }

        public Builder withAccountData(AccountData accountData) {
            this.accountData = accountData;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withBankIdData(String bankId, String bankIdCode) {
            this.bankId = bankId;
            this.bankIdCode = bankIdCode;
            return this;
        }

        public TransactionParty build() {
            return new TransactionParty(name, accountData, address, bankId, bankIdCode);
        }
    }

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "transaction_party_name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private AccountData accountData;

    @Column(name = "address")
    private String address;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "bank_id_code")
    private String bankIdCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "beneficiary_party_id")
    private FinancialTransactionAttributes financialTransactionAttributesForBeneficiary;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debtor_party_id")
    private FinancialTransactionAttributes financialTransactionAttributesForDebtor;

    //
    // Constructors, factory methods.
    //

    private TransactionParty(String name, AccountData accountData, String address, String bankId, String bankIdCode) {
        this.name = name;
        this.accountData = accountData;
        this.address = address;
        this.bankId = bankId;
        this.bankIdCode = bankIdCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public TransactionParty() {
        // For Hibernate.
    }

    //
    // Field accessors.
    //

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBankId() {
        return bankId;
    }

    public String getBankIdCode() {
        return bankIdCode;
    }

    public String getAccountName() {
        return accountData.getAccountName();
    }

    public String getAccountNumber() {
        return accountData.getAccountNumber();
    }

    public AccountNumberCode getAccountNumberCode() {
        return accountData.getAccountNumberCode();
    }

    public Integer getAccountType() {
        return accountData.getAccountType();
    }

    public void updateFields(AccountNumberCode accountNumberCode, String accountNumber, Integer accountType,
                             String accountName, String address, String bankId, String bankIdCode, String name) {
        if (this.accountData == null) {
            if (accountDataShouldBeUpdated(accountNumberCode, accountNumber, accountName, accountType)) {
                this.accountData = buildAccountData(accountNumberCode, accountNumber, accountName, accountType);
            }
        } else {
            this.accountData.updateFields(accountNumberCode, accountNumber, accountName, accountType);
        }

        if (address != null) {
            this.address = address;
        }
        if (bankId != null) {
            this.bankId = bankId;
        }
        if (bankIdCode != null) {
            this.bankIdCode = bankIdCode;
        }
        if (name != null) {
            this.name = name;
        }
    }

    private boolean accountDataShouldBeUpdated(AccountNumberCode accountNumberCode, String accountNumber, String accountName,
                                               Integer accountType) {
        return accountName != null || accountNumber != null || accountNumberCode != null || accountType != null;
    }

    private AccountData buildAccountData(AccountNumberCode accountNumberCode, String accountNumber, String accountName,
                                         Integer accountType) {
        if (accountName == null) {
            return new AccountData(accountNumber);
        }

        AccountData.Builder accountDataBuilder = AccountData.builder(accountName);
        if (accountNumberCode == AccountNumberCode.BBAN) {
            accountDataBuilder.withBbanNumber(accountNumber);
        } else if (accountNumberCode == AccountNumberCode.IBAN) {
            accountDataBuilder.withIbanNumber(accountNumber);
        }
        if (accountType != null) {
            accountDataBuilder.withType(accountType);
        }
        return accountDataBuilder.build();
    }
}
