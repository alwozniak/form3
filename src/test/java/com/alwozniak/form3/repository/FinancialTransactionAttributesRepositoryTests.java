package com.alwozniak.form3.repository;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.alwozniak.form3.domain.TransactionParty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        FinancialTransaction persistedTransaction = transactionRepository.save(
                FinancialTransaction.newPayment(UUID.randomUUID()));
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(persistedTransaction)
                .withBeneficiary(beneficiaryParty)
                .withDebtor(debtorParty)
                .build();
        FinancialTransactionAttributes persistedAttributes = repository.save(attributes);

        FinancialTransactionAttributes fetchedAttributes = repository.findById(persistedAttributes.getId())
                .orElseThrow(() -> new RuntimeException("FinancialTransactionAttributes record not found."));

        assertThat(fetchedAttributes.getBeneficiaryParty().getName(), is(beneficiaryName));
        assertThat(fetchedAttributes.getDebtorParty().getName(), is(debtorName));
    }
}
