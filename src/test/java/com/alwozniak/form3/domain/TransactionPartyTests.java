package com.alwozniak.form3.domain;

import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TransactionPartyTests {

    private static final String ACCOUNT_NAME = "W Owens";
    private static final String BBAN_ACCOUNT_NUMBER = "31926819";
    private static final String IBAN_ACCOUNT_NUMBER = "GB29XABC10161234567801";
    private static final String OWNERS_NAME = "Wilfred Jeremiah Owens";
    private static final String ADDRESS = "1 The Beneficiary Localtown SE2";
    private static final String BANK_ID = "403000";
    private static final String BANK_ID_CODE = "GBDSC";

    @Test
    public void shouldHaveAllFieldsSetCorrectlyWhenCreatingTransactionPartyWithBbanAccountNumber() {
        int accountType = 0;
        AccountData accountData = AccountData.builder(ACCOUNT_NAME)
                .withBbanNumber(BBAN_ACCOUNT_NUMBER)
                .withType(accountType)
                .build();

        TransactionParty transactionParty = buildTransactionPartyWithDefaultValuesAndAccountData(accountData);

        assertFieldsWithValues(transactionParty, ACCOUNT_NAME, BBAN_ACCOUNT_NUMBER, OWNERS_NAME, ADDRESS, BANK_ID,
                BANK_ID_CODE, AccountNumberCode.BBAN);
        assertThat(transactionParty.getAccountType(), is(accountType));
    }

    @Test
    public void shouldHaveAllFieldsSetCorrectlyWhenCreatingTransactionPartyWithIbanAccountNumber() {
        AccountData accountData = buildAccountDataWithIbanNumber();

        TransactionParty transactionParty = buildTransactionPartyWithDefaultValuesAndAccountData(accountData);

        assertFieldsWithValues(transactionParty, ACCOUNT_NAME, IBAN_ACCOUNT_NUMBER, OWNERS_NAME, ADDRESS, BANK_ID,
                BANK_ID_CODE, AccountNumberCode.IBAN);
    }

    @Test
    public void shouldNotModifyAnyFieldIfUpdatedWithNullValues() {
        AccountData accountData = buildAccountDataWithIbanNumber();
        TransactionParty transactionParty = buildTransactionPartyWithDefaultValuesAndAccountData(accountData);

        transactionParty.updateFields(null, null, null, null,
                null, null, null, null);

        assertFieldsWithValues(transactionParty, ACCOUNT_NAME, IBAN_ACCOUNT_NUMBER, OWNERS_NAME, ADDRESS, BANK_ID,
                BANK_ID_CODE, AccountNumberCode.IBAN);
    }

    @Test
    public void shouldModifyFieldWhenUpdatingWithNonNullValues() {
        AccountData accountData = buildAccountDataWithIbanNumber();
        TransactionParty transactionParty = buildTransactionPartyWithDefaultValuesAndAccountData(accountData);
        String newAccountName = "EJ Brown Black";
        String newOwnersName = "Emelia Jane Brown";
        String newAddress = "10 Debtor Crescent Sourcetown NE1";
        String newBankId = "203301";
        String newBankIdCode = "GBDSC";
        String newAccountNumber = "34567823";

        transactionParty.updateFields(AccountNumberCode.BBAN, newAccountNumber, 0, newAccountName,
                newAddress,  newBankId, newBankIdCode, newOwnersName);

        assertFieldsWithValues(transactionParty, newAccountName, newAccountNumber, newOwnersName, newAddress, newBankId,
                newBankIdCode, AccountNumberCode.BBAN);
    }

    @Test
    public void shouldCreateAccountDataWhenNotPresentAndUpdatingAccountDataValues() {
        TransactionParty transactionParty = TransactionParty.builder().build();

        transactionParty.updateFields(AccountNumberCode.IBAN, IBAN_ACCOUNT_NUMBER, null, ACCOUNT_NAME,
                null, null, null, null);

        assertThat(transactionParty.getAccountNumber(), is(IBAN_ACCOUNT_NUMBER));
    }

    //
    // Helper methods.
    //

    private TransactionParty buildTransactionPartyWithDefaultValuesAndAccountData(AccountData accountData) {
        return TransactionParty.builder()
                .withName(OWNERS_NAME)
                .withAccountData(accountData)
                .withAddress(ADDRESS)
                .withBankIdData(BANK_ID, BANK_ID_CODE)
                .build();
    }

    private AccountData buildAccountDataWithIbanNumber() {
        return AccountData.builder(ACCOUNT_NAME)
                .withIbanNumber(IBAN_ACCOUNT_NUMBER)
                .build();
    }

    private void assertFieldsWithValues(TransactionParty transactionParty, String accountName, String accountNumber,
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
