package com.alwozniak.form3.domain;

import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TransactionPartyTests {

    @Test
    public void shouldHaveAllFieldsSetCorrectlyWhenCreatingTransactionPartyWithBbanAccountNumber() {
        String accountName = "W Owens";
        String accountNumber = "31926819";
        int accountType = 0;
        AccountData accountData = AccountData.builder(accountName)
                .withBbanNumber(accountNumber)
                .withType(accountType)
                .build();
        String ownersName = "Wilfred Jeremiah Owens";
        String address = "1 The Beneficiary Localtown SE2";
        String bankId = "403000";
        String bankIdCode = "GBDSC";

        TransactionParty transactionParty = TransactionParty.builder()
                .withName(ownersName)
                .withAccountData(accountData)
                .withAddress(address)
                .withBankIdData(bankId, bankIdCode)
                .build();

        assertFieldsSetCorrectly(transactionParty, accountName, accountNumber, ownersName, address, bankId, bankIdCode,
                AccountNumberCode.BBAN);
        assertThat(transactionParty.getAccountType(), is(accountType));
    }

    @Test
    public void shouldHaveAllFieldsSetCorrectlyWhenCreatingTransactionPartyWithIbanAccountNumber() {
        String accountName = "EJ Brown Black";
        String accountNumber = "GB29XABC10161234567801";
        AccountData accountData = AccountData.builder(accountName)
                .withIbanNumber(accountNumber)
                .build();
        String ownersName = "Emelia Jane Brown";
        String address = "10 Debtor Crescent Sourcetown NE1";
        String bankId = "203301";
        String bankIdCode = "GBDSC";

        TransactionParty transactionParty = TransactionParty.builder()
                .withName(ownersName)
                .withAccountData(accountData)
                .withAddress(address)
                .withBankIdData(bankId, bankIdCode)
                .build();

        assertFieldsSetCorrectly(transactionParty, accountName, accountNumber, ownersName, address, bankId, bankIdCode,
                AccountNumberCode.IBAN);
    }

    //
    // Helper methods.
    //

    private void assertFieldsSetCorrectly(TransactionParty transactionParty, String accountName, String accountNumber,
                                          String ownersName, String address, String bankId, String bankIdCode,
                                          AccountNumberCode accountNumberCode) {
        assertThat(transactionParty.getName(), is(ownersName));
        assertThat(transactionParty.getAccountName(), is(accountName));
        assertThat(transactionParty.getAccountNumber(), is(accountNumber));
        assertThat(transactionParty.getAccountNumberCode(), is(accountNumberCode));
        assertThat(transactionParty.getAddress(), is(address));
        assertThat(transactionParty.getBankId(), is(bankId));
        assertThat(transactionParty.getBankIdCode(), is(bankIdCode));
    }
}
