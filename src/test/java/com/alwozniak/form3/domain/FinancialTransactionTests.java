package com.alwozniak.form3.domain;

import com.alwozniak.form3.domain.FinancialTransaction.FinancialTransactionType;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FinancialTransactionTests {

    @Test
    public void shouldCorrectlyCreatePaymentTransaction() {
        UUID organisationId = UUID.randomUUID();
        FinancialTransaction paymentTransaction = FinancialTransaction.newPayment(organisationId);

        assertThat(paymentTransaction.getTransactionType(), is(FinancialTransactionType.PAYMENT));
        assertThat(paymentTransaction.getVersion(), is(FinancialTransaction.DEFAULT_VERSION));
        assertThat(paymentTransaction.getOrganisationId(), is(organisationId));
    }
}
