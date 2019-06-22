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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private AccountData accountData;

    @Column(name = "address")
    private String address;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "bank_id_code")
    private String bankIdCode;

    @OneToOne(mappedBy = "beneficiaryParty")
    private FinancialTransactionAttributes financialTransactionAttributesForBeneficiary;

    @OneToOne(mappedBy = "debtorParty")
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
}
