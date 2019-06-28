package com.alwozniak.form3.domain;

import com.alwozniak.form3.domain.FinancialTransactionAttributes.*;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static com.alwozniak.form3.domain.FinancialTransactionAttributes.*;
import static com.alwozniak.form3.util.TestUtils.getDateOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class FinancialTransactionAttributesTests {

    private static final Double AMOUNT = 100.21;
    private static final String CURRENCY = "GBP";
    private static final String END_TO_END_REFERENCE = "Wil piano Jan";
    private static final String NUMERIC_REFERENCE = "1002001";
    private static final String PAYMENT_ID = "123456789012345678";
    private static final String PAYMENT_PURPOSE = "Paying for goods/services";
    private static final PaymentScheme PAYMENT_SCHEME = PaymentScheme.FPS;
    private static final PaymentType PAYMENT_TYPE = PaymentType.CREDIT;
    private static final Date PROCESSING_DATE = getDateOf(18, 1, 2017);
    private static final String REFERENCE = "Payment for Em's piano lessons";
    private static final SchemePaymentSubType SCHEME_PAYMENT_SUB_TYPE = SchemePaymentSubType.INTERNET_BANKING;
    private static final SchemePaymentType SCHEME_PAYMENT_TYPE = SchemePaymentType.IMMEDIATE_PAYMENT;

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

        FinancialTransactionAttributes attributes = createAttributesWithDefaultValues(transaction);

        assertDefaultValuesForAttributes(attributes);
    }


    @Test
    public void shouldCorrectlySetBeneficiaryAndDebtorParties() {
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());

        TransactionParty beneficiary = TransactionParty.builder().build();
        TransactionParty debtor = TransactionParty.builder().build();
        FinancialTransactionAttributes attributes = builder(transaction)
                .withBeneficiary(beneficiary)
                .withDebtor(debtor)
                .build();

        assertThat(attributes.getBeneficiaryParty(), is(beneficiary));
        assertThat(attributes.getDebtorParty(), is(debtor));
    }

    @Test
    public void shouldNotModifyFieldsWhenUpdatingWithNullValues() {
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        FinancialTransactionAttributes attributes = createAttributesWithDefaultValues(transaction);

        attributes.updateFields(null, null, null, null, null,
                null, null, null,null, null,
                null, null);

        assertDefaultValuesForAttributes(attributes);
    }

    @Test
    public void shouldModifyFieldsWhenUpdatingWithNotNullValues() {
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        FinancialTransactionAttributes attributes = createAttributesWithDefaultValues(transaction);
        Double newAmount = 90.89;
        String newCurrency = "CHF";
        String newEndToEndReference = "Modified end to end reference";
        String newNumericReference = "00998877";
        String newPaymentId = "0987654321654";
        String newPaymentPurpose = "Modified payment purpose";
        Date newProcessingDate = getDateOf(9, 9, 2017);
        String newReference = "Modified reference";

        attributes.updateFields(newAmount, newCurrency, newEndToEndReference, newNumericReference, newPaymentId,
                newPaymentPurpose, null, null, newProcessingDate, newReference,
                null, null);

        assertThat(attributes.getAmount(), is(newAmount));
        assertThat(attributes.getCurrency(), is(newCurrency));
        assertThat(attributes.getEndToEndReference(), is(newEndToEndReference));
        assertThat(attributes.getNumericReference(), is(newNumericReference));
        assertThat(attributes.getPaymentId(), is(newPaymentId));
        assertThat(attributes.getPaymentPurpose(), is(newPaymentPurpose));
        assertThat(attributes.getProcessingDate(), is(newProcessingDate));
        assertThat(attributes.getReference(), is(newReference));
    }

    //
    // Helper methods.
    //

    private FinancialTransactionAttributes createAttributesWithDefaultValues(FinancialTransaction transaction) {
        return builder(transaction)
                .withAmountInCurrency(AMOUNT, CURRENCY)
                .withEndToEndReference(END_TO_END_REFERENCE)
                .withNumericReference(NUMERIC_REFERENCE)
                .withPaymentData(PAYMENT_ID, PAYMENT_PURPOSE, PAYMENT_TYPE)
                .withProcessingDate(PROCESSING_DATE)
                .withReference(REFERENCE)
                .withPaymentSchemeData(PAYMENT_SCHEME, SCHEME_PAYMENT_TYPE, SCHEME_PAYMENT_SUB_TYPE)
                .build();
    }

    private void assertDefaultValuesForAttributes(FinancialTransactionAttributes attributes) {
        assertThat(attributes.getAmount(), is(AMOUNT));
        assertThat(attributes.getCurrency(), is(CURRENCY));
        assertThat(attributes.getEndToEndReference(), is(END_TO_END_REFERENCE));
        assertThat(attributes.getNumericReference(), is(NUMERIC_REFERENCE));
        assertThat(attributes.getPaymentId(), is(PAYMENT_ID));
        assertThat(attributes.getPaymentPurpose(), is(PAYMENT_PURPOSE));
        assertThat(attributes.getPaymentScheme(), is(PAYMENT_SCHEME));
        assertThat(attributes.getPaymentType(), is(PAYMENT_TYPE));
        assertThat(attributes.getProcessingDate(), is(PROCESSING_DATE));
        assertThat(attributes.getReference(), is(REFERENCE));
        assertThat(attributes.getSchemePaymentSubType(), is(SCHEME_PAYMENT_SUB_TYPE));
        assertThat(attributes.getSchemePaymentType(), is(SCHEME_PAYMENT_TYPE));
    }
}
