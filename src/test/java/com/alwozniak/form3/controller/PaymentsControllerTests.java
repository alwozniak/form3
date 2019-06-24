package com.alwozniak.form3.controller;

import com.alwozniak.form3.domain.*;
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
import static org.hamcrest.Matchers.*;
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
    public static final String PATH_TO_ATTRIBUTES = "$.data.attributes";

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

    @Test
    public void shouldReturnTransactionPartyResourcesForExistingPaymentWithBeneficiaryParty() throws Exception {
        String beneficiaryName = "Wilfred Jeremiah Owens";
        String beneficiaryAddress = "1 The Beneficiary Localtown SE2";
        String beneficiaryBankId = "403000";
        String beneficiaryBankIdCode = "GBDSC";
        String beneficiaryAccountName = "W Owens";
        String beneficiaryAccountNumber = "31926819";
        int beneficiaryAccountType = 0;
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        AccountData beneficiaryAccountData = AccountData.builder(beneficiaryAccountName)
                .withBbanNumber(beneficiaryAccountNumber)
                .withType(beneficiaryAccountType)
                .build();
        TransactionParty beneficiary = TransactionParty.builder()
                .withName(beneficiaryName)
                .withAddress(beneficiaryAddress)
                .withBankIdData(beneficiaryBankId, beneficiaryBankIdCode)
                .withAccountData(beneficiaryAccountData)
                .build();
        FinancialTransactionAttributes attributesWithBeneficiary = FinancialTransactionAttributes.builder(transaction)
                .withBeneficiary(beneficiary)
                .build();
        transaction.setAttributes(attributesWithBeneficiary);
        FinancialTransaction persistedPayment = repository.save(transaction);
        UUID existingPaymentId = persistedPayment.getId();

        String pathToBeneficiary = PATH_TO_ATTRIBUTES + ".beneficiary_party";
        mockMvc.perform(get("/payments/" + existingPaymentId.toString()).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath(pathToBeneficiary, notNullValue()))
                .andExpect(jsonPath(pathToBeneficiary + ".account_name", is(beneficiaryAccountName)))
                .andExpect(jsonPath(pathToBeneficiary + ".account_number", is(beneficiaryAccountNumber)))
        .andExpect(jsonPath(pathToBeneficiary + ".account_number_code", is("BBAN")))
        .andExpect(jsonPath(pathToBeneficiary + ".account_type", is(beneficiaryAccountType)))
        .andExpect(jsonPath(pathToBeneficiary + ".address", is(beneficiaryAddress)))
        .andExpect(jsonPath(pathToBeneficiary + ".bank_id", is(beneficiaryBankId)))
        .andExpect(jsonPath(pathToBeneficiary + ".bank_id_code", is (beneficiaryBankIdCode)))
        .andExpect(jsonPath(pathToBeneficiary + ".name", is(beneficiaryName)));
    }

    @Test
    public void shouldReturnTransactionPartyResourcesForExistingPaymentWithDebtorParty() throws Exception {
        String debtorName = "Emelia Jane Brown";
        String debtorAddress = "1 The Beneficiary Localtown SE2";
        String debtorBankId = "403000";
        String debtorBankIdCode = "GBDSC";
        String debtorAccountName = "W Owens";
        String debtorAccountNumber = "GB29XABC10161234567801";
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        AccountData debtorAccountData = AccountData.builder(debtorAccountName)
                .withIbanNumber(debtorAccountNumber)
                .build();
        TransactionParty debtor = TransactionParty.builder()
                .withName(debtorName)
                .withAddress(debtorAddress)
                .withBankIdData(debtorBankId, debtorBankIdCode)
                .withAccountData(debtorAccountData)
                .build();
        FinancialTransactionAttributes attributesWithDebtor = FinancialTransactionAttributes.builder(transaction)
                .withDebtor(debtor)
                .build();
        transaction.setAttributes(attributesWithDebtor);
        FinancialTransaction persistedPayment = repository.save(transaction);
        UUID existingPaymentId = persistedPayment.getId();

        String pathToDebtor = PATH_TO_ATTRIBUTES + ".debtor_party";
        mockMvc.perform(get("/payments/" + existingPaymentId.toString()).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath(pathToDebtor, notNullValue()))
                .andExpect(jsonPath(pathToDebtor + ".account_name", is(debtorAccountName)))
                .andExpect(jsonPath(pathToDebtor + ".account_number", is(debtorAccountNumber)))
                .andExpect(jsonPath(pathToDebtor + ".account_number_code", is("IBAN")))
                .andExpect(jsonPath(pathToDebtor + ".address", is(debtorAddress)))
                .andExpect(jsonPath(pathToDebtor + ".bank_id", is(debtorBankId)))
                .andExpect(jsonPath(pathToDebtor + ".bank_id_code", is (debtorBankIdCode)))
                .andExpect(jsonPath(pathToDebtor + ".name", is(debtorName)));
    }

    @Test
    public void shouldReturnForeignExchangeInfoForExistingTransactionContainingIt() throws Exception {
        String contractReference = "FX123";
        Double exchangeRate = 2.00;
        Double originalAmount = 123.45;
        String originalCurrency = "USD";
        ForeignExchangeInfo foreignExchangeInfo = new ForeignExchangeInfo(contractReference, exchangeRate,
                originalAmount, originalCurrency);
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(transaction)
                .withForeignExchangeInfo(foreignExchangeInfo)
                .build();
        transaction.setAttributes(attributes);
        FinancialTransaction persistedPayment = repository.save(transaction);
        UUID existingPaymentId = persistedPayment.getId();

        String pathToFx = PATH_TO_ATTRIBUTES + ".fx";
        mockMvc.perform(get("/payments/" + existingPaymentId.toString()).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath(pathToFx, notNullValue()))
                .andExpect(jsonPath(pathToFx + ".contract_reference", is(contractReference)))
                .andExpect(jsonPath(pathToFx + ".exchange_rate", is("2.00000")))
                .andExpect(jsonPath(pathToFx + ".original_amount", is("123.45")))
                .andExpect(jsonPath(pathToFx + ".original_currency", is(originalCurrency)));
    }
}
