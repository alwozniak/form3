package com.alwozniak.form3.repository;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransaction.FinancialTransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FinancialTransactionRepositoryTest {

    @Autowired
    private FinancialTransactionRepository transactionRepository;

    @Test
    public void shouldReturnEmptyOptionalWhenFinancialTransactionDoesNotExist() {
        Optional<FinancialTransaction> fetchedTransaction = transactionRepository.findById(UUID.randomUUID());

        assertFalse(fetchedTransaction.isPresent());
    }

    @Test
    public void shouldCorrectlyReturnSavedPaymentTransaction() {
        UUID existingTransactionId = getIdOfNewPersistedFinancialTransaction(UUID.randomUUID());

        Optional<FinancialTransaction> fetchedTransaction = transactionRepository.findById(existingTransactionId);

        assertTrue(fetchedTransaction.isPresent());
    }

    @Test
    public void paymentTransactionSavedAndFetchedShouldHaveAllFieldsSet() {
        UUID organisationId = UUID.randomUUID();
        UUID existingTransactionId = getIdOfNewPersistedFinancialTransaction(organisationId);

        FinancialTransaction fetchedTransaction = transactionRepository.findById(existingTransactionId)
                .orElseThrow(RuntimeException::new);

        assertThat(fetchedTransaction.getId(), notNullValue());
        assertThat(fetchedTransaction.getOrganisationId(), is(organisationId));
        assertThat(fetchedTransaction.getTransactionType(), is(FinancialTransactionType.PAYMENT));
        assertThat(fetchedTransaction.getVersion(), is(FinancialTransaction.DEFAULT_VERSION));
    }

    //
    // Helper methods.
    //

    private UUID getIdOfNewPersistedFinancialTransaction(UUID organisationId) {
        FinancialTransaction transaction = FinancialTransaction.newPayment(organisationId);
        FinancialTransaction persistedTransaction = transactionRepository.save(transaction);
        return persistedTransaction.getId();
    }
}
