package com.alwozniak.form3.controller;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentScheme;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentSubType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentType;
import com.alwozniak.form3.repository.FinancialTransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static com.alwozniak.form3.util.TestUtils.getDateOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentsControllerTests {

    @Autowired
    private FinancialTransactionRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        repository.deleteAllInBatch();
    }

    //
    // Tests for GET /payments.
    //

    @Test
    public void shouldCorrectlyFetchEmptyListOfPayments() throws Exception {
        mockMvc.perform(get("/payments").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"data\":[]}"));
    }

    //
    // Tests for GET /payments/:id.
    //

    @Test
    public void shouldReturnNotFoundStatusCodeWhenPaymentOfAGivenIdDoesNotExist() throws Exception {
        UUID paymentId = UUID.randomUUID();
        mockMvc.perform(get("/payments/" + paymentId).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessage", containsString(paymentId.toString())))
                .andExpect(jsonPath("$.errorMessage", containsString("not found")));
    }

    @Test
    public void shouldReturnBadRequestStatusWithErrorMessageWhenPaymentIdIsOfIncorrectType() throws Exception {
        mockMvc.perform(get("/payments/123").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessage", containsString("Bad request data.")));
    }

    @Test
    public void shouldReturnAValidPaymentResourceWhenAPaymentWithGivenIdExists() throws Exception {
        UUID organisationId = UUID.randomUUID();
        FinancialTransaction transaction = FinancialTransaction.newPayment(organisationId);
        FinancialTransaction persistedPayment = repository.save(transaction);
        UUID existingPaymentId = persistedPayment.getId();

        mockMvc.perform(get("/payments/" + existingPaymentId.toString()).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type", is("Payment")))
                .andExpect(jsonPath("$.data.id", is(existingPaymentId.toString())))
                .andExpect(jsonPath("$.data.organisation_id", is(organisationId.toString())))
                .andExpect(jsonPath("$.data.version", is(0)));
    }

    @Test
    public void shouldReturnAttributeValuesForExistingPaymentWithAttributes() throws Exception {
        double paymentAmount = 123.45;
        String paymentCurrency = "USD";
        String endToEndReference = "This is end to end reference string.";
        String numericReference = "10098";
        String paymentId = "123456789012345678";
        String paymentPurpose = "This is payment purpose string.";
        UUID organisationId = UUID.randomUUID();
        FinancialTransaction transaction = FinancialTransaction.newPayment(organisationId);
        Date processingDate = getDateOf(10, 5, 2019);
        String reference = "This is reference string";
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(transaction)
                .withAmountInCurrency(paymentAmount, paymentCurrency)
                .withEndToEndReference(endToEndReference)
                .withNumericReference(numericReference)
                .withPaymentData(paymentId, paymentPurpose, PaymentType.CREDIT)
                .withPaymentSchemeData(PaymentScheme.FPS, SchemePaymentType.IMMEDIATE_PAYMENT,
                        SchemePaymentSubType.INTERNET_BANKING)
                .withProcessingDate(processingDate)
                .withReference(reference)
                .build();
        transaction.setAttributes(attributes);
        FinancialTransaction persistedPayment = repository.save(transaction);
        UUID existingPaymentId = persistedPayment.getId();

        String pathToAttributes = "$.data.attributes";
        mockMvc.perform(get("/payments/" + existingPaymentId.toString()).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath(pathToAttributes + ".amount", is(paymentAmount)))
                .andExpect(jsonPath(pathToAttributes + ".currency", is(paymentCurrency)))
                .andExpect(jsonPath(pathToAttributes + ".end_to_end_reference", is(endToEndReference)))
                .andExpect(jsonPath(pathToAttributes + ".numeric_reference", is(numericReference)))
                .andExpect(jsonPath(pathToAttributes + ".payment_id", is(paymentId)))
                .andExpect(jsonPath(pathToAttributes + ".payment_purpose", is(paymentPurpose)))
                .andExpect(jsonPath(pathToAttributes + ".payment_scheme", is("FPS")))
                .andExpect(jsonPath(pathToAttributes + ".payment_type", is("Credit")))
                .andExpect(jsonPath(pathToAttributes + ".processing_date", is("2019-05-10")))
                .andExpect(jsonPath(pathToAttributes + ".reference", is(reference)))
                .andExpect(jsonPath(pathToAttributes + ".scheme_payment_sub_type", is("InternetBanking")))
                .andExpect(jsonPath(pathToAttributes + ".scheme_payment_type", is("ImmediatePayment")));
    }
}
