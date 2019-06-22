package com.alwozniak.form3.controller;

import com.alwozniak.form3.domain.FinancialTransaction;
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

import java.util.UUID;

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
}
