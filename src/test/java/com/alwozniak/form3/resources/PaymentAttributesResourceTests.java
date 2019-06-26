package com.alwozniak.form3.resources;

import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentScheme;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentSubType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentType;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PaymentAttributesResourceTests {

    @Test
    public void shouldCorrectlyConvertAmountFromStringToDouble() {
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();

        paymentAttributesResource.setAmountFromString("100.21");

        assertThat(paymentAttributesResource.getAmount(), is(100.21));
    }

    @Test
    public void shouldCorrectlyConvertPaymentSchemeFromString() {
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();

        paymentAttributesResource.setPaymentSchemeFromString("FPS");

        assertThat(paymentAttributesResource.getPaymentScheme(), is(PaymentScheme.FPS));
    }

    @Test
    public void shouldCorrectlySetProcessingDateFromString() throws ParseException {
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();
        LocalDate localDate = LocalDate.of(2019, 3, 12);
        Date expectedDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        paymentAttributesResource.setProcessingDateFromString("2019-03-12");

        assertThat(paymentAttributesResource.getProcessingDate(), is(expectedDate));
    }

    @Test
    public void shouldCorrectlySetPaymentTypeFromString() {
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();

        paymentAttributesResource.setPaymentTypeFromString("Credit");

        assertThat(paymentAttributesResource.getPaymentType(), is(PaymentType.CREDIT));
    }

    @Test
    public void shouldCorrectlySetSchemePaymentTypeFromString() {
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();

        paymentAttributesResource.setSchemePaymentTypeFromString("ImmediatePayment");

        assertThat(paymentAttributesResource.getSchemePaymentType(), is(SchemePaymentType.IMMEDIATE_PAYMENT));
    }

    @Test
    public void shouldCorrectlySetSchemePaymentSubTypeFromString() {
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();

        paymentAttributesResource.setSchemePaymentSubTypeFromString("InternetBanking");

        assertThat(paymentAttributesResource.getSchemePaymentSubType(), is(SchemePaymentSubType.INTERNET_BANKING));
    }
}
