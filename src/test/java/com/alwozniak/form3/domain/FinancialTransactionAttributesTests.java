package com.alwozniak.form3.domain;

import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentScheme;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static com.alwozniak.form3.domain.FinancialTransactionAttributes.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class FinancialTransactionAttributesTests {

    @Test
    public void shouldCreateEmptyFinancialTransactionsAttributes() {
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        FinancialTransactionAttributes attributes = builder(transaction).build();

        assertThat(attributes.getId(), is(nullValue()));
        assertThat(attributes.getTransaction(), is(transaction));
    }

    @Test
    public void shouldCorrectlySetSimpleTypeAttributesByBuilder() {
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        Double amount = 100.21;
        String currency = "GBP";
        String endToEndReference = "Wil piano Jan";
        String numericReference = "1002001";
        String paymentId = "123456789012345678";
        String paymentPurpose = "Paying for goods/services";
        PaymentScheme paymentScheme = PaymentScheme.FPS;
        PaymentType paymentType = PaymentType.CREDIT;
        Date processingDate = getDateOf(18, 1, 2017);
        String reference = "Payment for Em's piano lessons";
        SchemePaymentSubType schemePaymentSubType = SchemePaymentSubType.INTERNET_BANKING;
        SchemePaymentType schemePaymentType = SchemePaymentType.IMMEDIATE_PAYMENT;

        FinancialTransactionAttributes attributes = builder(transaction)
                .withAmountInCurrency(amount, currency)
                .withEndToEndReference(endToEndReference)
                .withNumericReference(numericReference)
                .withPaymentData(paymentId, paymentPurpose, paymentType)
                .withProcessingDate(processingDate)
                .withReference(reference)
                .withPaymentSchemeData(paymentScheme, schemePaymentType, schemePaymentSubType)
                .build();

        assertThat(attributes.getAmount(), is(amount));
        assertThat(attributes.getCurrency(), is(currency));
        assertThat(attributes.getEndToEndReference(), is(endToEndReference));
        assertThat(attributes.getNumericReference(), is(numericReference));
        assertThat(attributes.getPaymentId(), is(paymentId));
        assertThat(attributes.getPaymentPurpose(), is(paymentPurpose));
        assertThat(attributes.getPaymentScheme(), is(paymentScheme));
        assertThat(attributes.getPaymentType(), is(paymentType));
        assertThat(attributes.getProcessingDate(), is(processingDate));
        assertThat(attributes.getReference(), is(reference));
        assertThat(attributes.getSchemePaymentSubType(), is(schemePaymentSubType));
        assertThat(attributes.getSchemePaymentType(), is(schemePaymentType));
    }

    //
    // Helper methods.
    //

    private Date getDateOf(int dayOfMonth, int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }
}
