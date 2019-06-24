package com.alwozniak.form3.repository;

import com.alwozniak.form3.domain.*;
import com.alwozniak.form3.domain.ChargesInformation.ChargeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FinancialTransactionAttributesRepositoryTests {

    @Autowired
    private FinancialTransactionAttributesRepository repository;

    @Autowired
    private FinancialTransactionRepository transactionRepository;

    @Test
    public void shouldCorrectlyFetchDebtorAndBeneficiaryPartiesFromPersistedAttributesInstance() {
        String beneficiaryName = "Alice Beneficiary";
        String debtorName = "John Debtor";
        TransactionParty beneficiaryParty = TransactionParty.builder().withName(beneficiaryName).build();
        TransactionParty debtorParty = TransactionParty.builder().withName(debtorName).build();
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(getPersistedTransaction())
                .withBeneficiary(beneficiaryParty)
                .withDebtor(debtorParty)
                .build();
        FinancialTransactionAttributes persistedAttributes = repository.save(attributes);

        FinancialTransactionAttributes fetchedAttributes = fetchAttributesOrThrowException(persistedAttributes);

        assertThat(fetchedAttributes.getBeneficiaryParty().getName(), is(beneficiaryName));
        assertThat(fetchedAttributes.getDebtorParty().getName(), is(debtorName));
    }

    @Test
    public void shouldCorrectlyFetchForeignExchangeInfoFromPersistedAttributes() {
        String contractReference = "FX123";
        Double exchangeRate = 2.00000;
        Double originalAmount = 200.42;
        String originalCurrency = "USD";
        ForeignExchangeInfo foreignExchangeInfo = new ForeignExchangeInfo(contractReference, exchangeRate,
                originalAmount, originalCurrency);
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(getPersistedTransaction())
                .withForeignExchangeInfo(foreignExchangeInfo)
                .build();
        foreignExchangeInfo.setFinancialTransactionAttributes(attributes);
        FinancialTransactionAttributes persistedAttributes = repository.save(attributes);

        FinancialTransactionAttributes fetchedAttributes = fetchAttributesOrThrowException(persistedAttributes);

        ForeignExchangeInfo foreignExchangeInfoFromAttributes = fetchedAttributes.getForeignExchangeInfo();
        assertThat(foreignExchangeInfoFromAttributes, notNullValue());
        assertThat(foreignExchangeInfoFromAttributes.getContractReference(), is(contractReference));
        assertThat(foreignExchangeInfoFromAttributes.getExchangeRate(), is(exchangeRate));
        assertThat(foreignExchangeInfoFromAttributes.getOriginalAmount(), is(originalAmount));
        assertThat(foreignExchangeInfoFromAttributes.getOriginalCurrency(), is(originalCurrency));
    }

    @Test
    public void shouldCorrectlyFetchChargesInformationFromSavedAttributes() {
        String senderCurrency = "GBP";
        Double senderChargeInSenderCurrency = 5.00;
        Double senderChargeInReceiverCurrency = 10.00;
        String receiverCurrency = "USD";
        Double receiverChargesAmount = 1.00;
        ChargesInformation chargesInformation = ChargesInformation.builder(ChargeType.SHARED)
                .withReceiverCharge(receiverChargesAmount, receiverCurrency)
                .withSenderCharge(senderChargeInSenderCurrency, senderCurrency)
                .withSenderCharge(senderChargeInReceiverCurrency, receiverCurrency)
                .build();
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(getPersistedTransaction())
                .withChargesInformation(chargesInformation)
                .build();
        chargesInformation.setFinancialTransactionAttributes(attributes);
        FinancialTransactionAttributes persistedAttributes = repository.save(attributes);

        FinancialTransactionAttributes fetchedAttributes = fetchAttributesOrThrowException(persistedAttributes);

        ChargesInformation chargesInformationFromAttributes = fetchedAttributes.getChargesInformation();
        assertThat(chargesInformationFromAttributes, notNullValue());
        assertThat(chargesInformationFromAttributes.getReceiverChargesAmount(), is(receiverChargesAmount));
        assertThat(chargesInformationFromAttributes.getReceiverChargesCurrency(), is(receiverCurrency));
        assertThat(chargesInformationFromAttributes.getBearerCode(), is(ChargeType.SHARED));
        assertThat(chargesInformationFromAttributes.getSenderCharges().size(), is(2));
    }

    @Test
    public void shouldCorrectlyFetchSponsorPartyFromSavedAttributes() {
        String bankId = "123123";
        String bankIdCode = "GBDSC";
        String accountNumber = "56781234";
        AccountData accountData = new AccountData(accountNumber);
        TransactionParty sponsorParty = TransactionParty.builder()
                .withBankIdData(bankId, bankIdCode)
                .withAccountData(accountData)
                .build();
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(getPersistedTransaction())
                .withSponsorParty(sponsorParty)
                .build();
        FinancialTransactionAttributes persistedAttributes = repository.save(attributes);

        FinancialTransactionAttributes fetchedAttributes = fetchAttributesOrThrowException(persistedAttributes);

        TransactionParty extractedSponsorParty = fetchedAttributes.getSponsorParty();
        assertThat(extractedSponsorParty.getAccountNumber(), is(accountNumber));
        assertThat(extractedSponsorParty.getBankId(), is(bankId));
        assertThat(extractedSponsorParty.getBankIdCode(), is(bankIdCode));
    }

    //
    // Helper methods.
    //

    private FinancialTransaction getPersistedTransaction() {
        return transactionRepository.save(
                FinancialTransaction.newPayment(UUID.randomUUID()));
    }

    private FinancialTransactionAttributes fetchAttributesOrThrowException(FinancialTransactionAttributes attributes) {
        return repository.findById(attributes.getId())
                .orElseThrow(() -> new RuntimeException("FinancialTransactionAttributes record not found."));
    }
}
