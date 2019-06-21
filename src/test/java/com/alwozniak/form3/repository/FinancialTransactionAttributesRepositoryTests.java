package com.alwozniak.form3.repository;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.alwozniak.form3.domain.ForeignExchangeInfo;
import com.alwozniak.form3.domain.TransactionParty;
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

    @Autowired
    private ForeignExchangeInfoRepository fxInfoRepository;

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

        FinancialTransactionAttributes fetchedAttributes = repository.findById(persistedAttributes.getId())
                .orElseThrow(() -> new RuntimeException("FinancialTransactionAttributes record not found."));

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
        fxInfoRepository.save(foreignExchangeInfo);
        FinancialTransactionAttributes persistedAttributes = repository.save(attributes);

        FinancialTransactionAttributes fetchedAttributes = repository.findById(persistedAttributes.getId())
                .orElseThrow(() -> new RuntimeException("FinancialTransactionAttributes record not found."));

        ForeignExchangeInfo foreignExchangeInfoFromAttributes = fetchedAttributes.getForeignExchangeInfo();
        assertThat(foreignExchangeInfoFromAttributes, notNullValue());
        assertThat(foreignExchangeInfoFromAttributes.getContractReference(), is(contractReference));
        assertThat(foreignExchangeInfoFromAttributes.getExchangeRate(), is(exchangeRate));
        assertThat(foreignExchangeInfoFromAttributes.getOriginalAmount(), is(originalAmount));
        assertThat(foreignExchangeInfoFromAttributes.getOriginalCurrency(), is(originalCurrency));
    }

    //
    // Helper methods.
    //

    private FinancialTransaction getPersistedTransaction() {
        return transactionRepository.save(
                FinancialTransaction.newPayment(UUID.randomUUID()));
    }
}
