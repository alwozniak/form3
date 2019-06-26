package com.alwozniak.form3.service;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransaction.FinancialTransactionType;
import com.alwozniak.form3.repository.FinancialTransactionRepository;
import com.alwozniak.form3.resources.PaymentResourceData;
import com.alwozniak.form3.resources.PaymentsListResource;
import com.alwozniak.form3.resources.SinglePaymentResource;
import com.alwozniak.form3.service.exception.PaymentNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentsServiceTests {

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Before
    public void setUp() {
        financialTransactionRepository.deleteAllInBatch();
    }

    //
    // Tests for creating resources from FinancialTransaction entities.
    //

    @Test
    public void shouldCorrectlyConstructAResourceWithEmptyPaymentsList() {
        assertThat(financialTransactionRepository.findAllPayments(), is(empty()));

        PaymentsListResource resource = paymentsService.getPaymentsListResource();

        assertThat(resource.getData(), is(empty()));
    }

    @Test
    public void shouldCorrectlyConstructPaymentsListResourceWhenManyPaymentsArePresent() {
        int paymentsCount = 4;
        for (int i = 0; i < paymentsCount; i++) {
            financialTransactionRepository.save(FinancialTransaction.newPayment(UUID.randomUUID()));
        }

        PaymentsListResource resource = paymentsService.getPaymentsListResource();

        assertThat(resource.getData().size(), is(paymentsCount));
    }

    @Test(expected = PaymentNotFoundException.class)
    public void shouldThrowPaymentNotFoundExceptionWhenRequestingToCreateAResourceOfNonExistingPaymente()
            throws PaymentNotFoundException {
        paymentsService.getSinglePaymentResource(UUID.randomUUID());
    }

    @Test
    public void shouldCreateSinglePaymentResourceWhenPaymentIsPresent() throws PaymentNotFoundException {
        UUID organisationId = UUID.randomUUID();
        FinancialTransaction existingPayment = financialTransactionRepository.save(
                FinancialTransaction.newPayment(organisationId));

        SinglePaymentResource resource = paymentsService.getSinglePaymentResource(existingPayment.getId());

        PaymentResourceData resourceData = resource.getData();
        assertThat(resourceData.getType(), is("Payment"));
        assertThat(resourceData.getId(), is(existingPayment.getId().toString()));
        assertThat(resourceData.getOrganisationId(), is(organisationId));
    }

    //
    // Tests for creating new payment from PaymentResourceData.
    //

    @Test
    public void shouldCreateNewPaymentFromPaymentResourceData() {
        int version = 0;
        UUID organisationId = UUID.randomUUID();
        PaymentResourceData paymentResourceData = new PaymentResourceData();
        paymentResourceData.setTypeFromString("Payment");
        paymentResourceData.setVersion(version);
        paymentResourceData.setOrganisationIdFromString(organisationId.toString());

        FinancialTransaction payment = paymentsService.createNewPaymentFromResource(paymentResourceData);

        assertThat(payment.getId(), notNullValue());
        assertTrue(financialTransactionRepository.findById(payment.getId()).isPresent());
        assertThat(payment.getVersion(), is(version));
        assertThat(payment.getTransactionType(), is(FinancialTransactionType.PAYMENT));
        assertThat(payment.getOrganisationId(), is(organisationId));
    }
}
