package com.alwozniak.form3.controller;

import com.alwozniak.form3.domain.*;
import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import com.alwozniak.form3.domain.ChargesInformation.ChargeType;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.alwozniak.form3.util.TestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentsControllerTests {

    private static final String PATH_TO_ATTRIBUTES = "$.data.attributes";
    private static final String PAYMENTS_PATH = "/payments";

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
        mockMvc.perform(get(PAYMENTS_PATH).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"data\":[]}"));
    }

    @Test
    public void shouldCorrectlyFetchSingletonListOfPayments() throws Exception {
        FinancialTransaction persistedPayment = repository.save(FinancialTransaction.newPayment(UUID.randomUUID()));

        mockMvc.perform(get(PAYMENTS_PATH).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data.length()", is(1)))
                .andExpect(jsonPath("$.data[0].id", is(persistedPayment.getId().toString())));
    }

    @Test
    public void shouldCorrectlyFetchListOfMultiplePayments() throws Exception {
        List<FinancialTransaction> transactions = new ArrayList<>();
        int numberOfTransactions = 4;
        for (int i = 0; i < numberOfTransactions; i++) {
            transactions.add(repository.save(FinancialTransaction.newPayment(UUID.randomUUID())));
        }
        String[] transactionIds = transactions.stream()
                .map(FinancialTransaction::getId)
                .map(UUID::toString)
                .toArray(String[]::new);

        mockMvc.perform(get(PAYMENTS_PATH).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data.length()", is(numberOfTransactions)))
                .andExpect(jsonPath("$.data..id", contains(transactionIds)));
    }

    //
    // Tests for GET /payments/:id.
    //

    @Test
    public void shouldReturnNotFoundStatusCodeWhenPaymentOfAGivenIdDoesNotExist() throws Exception {
        UUID paymentId = UUID.randomUUID();
        mockMvc.perform(get(PAYMENTS_PATH + "/" + paymentId).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessage", containsString(paymentId.toString())))
                .andExpect(jsonPath("$.errorMessage", containsString("not found")));
    }

    @Test
    public void shouldReturnBadRequestStatusWithErrorMessageWhenPaymentIdIsOfIncorrectType() throws Exception {
        mockMvc.perform(get(PAYMENTS_PATH + "/123").accept(MediaType.APPLICATION_JSON_UTF8))
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

        mockMvc.perform(get(PAYMENTS_PATH + "/" + existingPaymentId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
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
        mockMvc.perform(get(PAYMENTS_PATH + "/" + existingPaymentId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
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
        mockMvc.perform(get(PAYMENTS_PATH + "/" + existingPaymentId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
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
        mockMvc.perform(get(PAYMENTS_PATH + "/" + existingPaymentId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
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
        mockMvc.perform(get(PAYMENTS_PATH + "/" + existingPaymentId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath(pathToFx, notNullValue()))
                .andExpect(jsonPath(pathToFx + ".contract_reference", is(contractReference)))
                .andExpect(jsonPath(pathToFx + ".exchange_rate", is("2.00000")))
                .andExpect(jsonPath(pathToFx + ".original_amount", is("123.45")))
                .andExpect(jsonPath(pathToFx + ".original_currency", is(originalCurrency)));
    }

    @Test
    public void shouldReturnChargesInformationResourceForExistingPaymentWithChargesInformation() throws Exception {
        ChargesInformation chargesInformation = ChargesInformation.builder(ChargeType.SHARED)
                .withSenderCharge(12.67, "USD")
                .withSenderCharge(8.00, "GBP")
                .withReceiverCharge(5.43, "USD")
                .build();
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        FinancialTransactionAttributes attributes = FinancialTransactionAttributes.builder(transaction)
                .withChargesInformation(chargesInformation)
                .build();
        transaction.setAttributes(attributes);
        FinancialTransaction persistedPayment = repository.save(transaction);
        UUID existingPaymentId = persistedPayment.getId();

        String pathToChargesInformation = PATH_TO_ATTRIBUTES + ".charges_information";
        mockMvc.perform(get(PAYMENTS_PATH + "/" + existingPaymentId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath(pathToChargesInformation, notNullValue()))
                .andExpect(jsonPath(pathToChargesInformation + ".bearer_code", is("SHAR")))
                .andExpect(jsonPath(pathToChargesInformation + ".receiver_charges_amount", is("5.43")))
                .andExpect(jsonPath(pathToChargesInformation + ".receiver_charges_currency", is("USD")))
                .andExpect(jsonPath(pathToChargesInformation + ".sender_charges.length()", is(2)))
                .andExpect(jsonPath(pathToChargesInformation + ".sender_charges[?(@.currency=='USD')].amount",
                        contains("12.67")))
                .andExpect(jsonPath(pathToChargesInformation + ".sender_charges[?(@.currency=='GBP')].amount",
                        contains("8.00")));
    }

    @Test
    public void shouldReturnTransactionPartyResourcesForExistingPaymentWithSponsorParty() throws Exception {
        String debtorBankId = "403000";
        String debtorBankIdCode = "GBDSC";
        String debtorAccountNumber = "GB29XABC10161234567801";
        FinancialTransaction transaction = FinancialTransaction.newPayment(UUID.randomUUID());
        AccountData debtorAccountData = new AccountData(debtorAccountNumber);
        TransactionParty sponsorParty = TransactionParty.builder()
                .withBankIdData(debtorBankId, debtorBankIdCode)
                .withAccountData(debtorAccountData)
                .build();
        FinancialTransactionAttributes attributesWithSponsor = FinancialTransactionAttributes.builder(transaction)
                .withSponsorParty(sponsorParty)
                .build();
        transaction.setAttributes(attributesWithSponsor);
        FinancialTransaction persistedPayment = repository.save(transaction);
        UUID existingPaymentId = persistedPayment.getId();

        String pathToSponsor = PATH_TO_ATTRIBUTES + ".sponsor_party";
        mockMvc.perform(get(PAYMENTS_PATH + "/" + existingPaymentId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath(pathToSponsor, notNullValue()))
                .andExpect(jsonPath(pathToSponsor + ".account_number", is(debtorAccountNumber)))
                .andExpect(jsonPath(pathToSponsor + ".bank_id", is(debtorBankId)))
                .andExpect(jsonPath(pathToSponsor + ".bank_id_code", is (debtorBankIdCode)));
    }

    //
    // Tests for POST /payments.
    //

    @Test
    public void shouldPersistValidPaymentsResourceWithoutAttributes() throws Exception {
        byte[] requestBody = getTestResource("payment-without-attributes.json");

        assertThat(repository.findAllPayments(), is(empty()));
        mockMvc.perform(post(PAYMENTS_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isCreated());
        FinancialTransaction persistedPayment = getTheOnlyPersistedTransaction();
        assertThat(persistedPayment.getVersion(), is(0));
        assertThat(persistedPayment.getTransactionType(), is(FinancialTransaction.FinancialTransactionType.PAYMENT));
        assertThat(persistedPayment.getOrganisationId().toString(), is("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"));
    }

    @Test
    public void shouldPersistValidPaymentsResourceWithAttributes() throws Exception {
        byte[] requestBody = getTestResource("payment-with-attributes.json");
        LocalDate localDate = LocalDate.of(2017, 1, 18);
        Date expectedDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertThat(repository.findAllPayments(), is(empty()));

        mockMvc.perform(post(PAYMENTS_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isCreated());
        FinancialTransaction paymentWithAttributes = getTheOnlyPersistedTransaction();
        FinancialTransactionAttributes attributes = paymentWithAttributes.getAttributes();
        assertThat(attributes.getAmount(), is(100.21));
        assertThat(attributes.getCurrency(), is("GBP"));
        assertThat(attributes.getEndToEndReference(), is("Wil piano Jan"));
        assertThat(attributes.getNumericReference(), is("1002001"));
        assertThat(attributes.getPaymentId(), is("123456789012345678"));
        assertThat(attributes.getPaymentPurpose(), is("Paying for goods/services"));
        assertThat(attributes.getPaymentScheme(), is(PaymentScheme.FPS));
        assertThat(attributes.getPaymentType(), is(PaymentType.CREDIT));
        assertThat(attributes.getProcessingDate(), is(expectedDate));
        assertThat(attributes.getReference(), is("Payment for Em's piano lessons"));
        assertThat(attributes.getPaymentType(), is(PaymentType.CREDIT));
        assertThat(attributes.getSchemePaymentType(), is(SchemePaymentType.IMMEDIATE_PAYMENT));
        assertThat(attributes.getSchemePaymentSubType(), is(SchemePaymentSubType.INTERNET_BANKING));
    }

    @Test
    public void shouldPersistPaymentWithAttributesContainingBeneficiaryParty() throws Exception {
        byte[] requestBody = getTestResource("payment-with-attributes-with-beneficiary.json");
        assertThat(repository.findAllPayments(), is(empty()));

        mockMvc.perform(post(PAYMENTS_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isCreated());
        FinancialTransaction transaction = getTheOnlyPersistedTransaction();
        FinancialTransactionAttributes attributes = transaction.getAttributes();
        TransactionParty beneficiaryParty = attributes.getBeneficiaryParty();
        assertThat(beneficiaryParty.getAccountName(), is("W Owens"));
        assertThat(beneficiaryParty.getAccountNumber(), is("31926819"));
        assertThat(beneficiaryParty.getAccountNumberCode(), is(AccountNumberCode.BBAN));
        assertThat(beneficiaryParty.getAccountType(), is(0));
        assertThat(beneficiaryParty.getAddress(), is("1 The Beneficiary Localtown SE2"));
        assertThat(beneficiaryParty.getBankId(), is("403000"));
        assertThat(beneficiaryParty.getBankIdCode(), is("GBDSC"));
        assertThat(beneficiaryParty.getName(), is("Wilfred Jeremiah Owens"));
    }

    @Test
    public void shouldPersistPaymentWithAttributesContainingDebtorParty() throws Exception {
        byte[] requestBody = getTestResource("payment-with-attributes-with-debtor.json");
        assertThat(repository.findAllPayments(), is(empty()));

        mockMvc.perform(post(PAYMENTS_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isCreated());
        FinancialTransaction transaction = getTheOnlyPersistedTransaction();
        FinancialTransactionAttributes attributes = transaction.getAttributes();
        TransactionParty debtorParty = attributes.getDebtorParty();
        assertThat(debtorParty.getAccountName(), is("EJ Brown Black"));
        assertThat(debtorParty.getAccountNumber(), is("GB29XABC10161234567801"));
        assertThat(debtorParty.getAccountNumberCode(), is(AccountNumberCode.IBAN));
        assertThat(debtorParty.getAddress(), is("10 Debtor Crescent Sourcetown NE1"));
        assertThat(debtorParty.getBankId(), is("203301"));
        assertThat(debtorParty.getBankIdCode(), is("GBDSC"));
        assertThat(debtorParty.getName(), is("Emelia Jane Brown"));
    }

    @Test
    public void shouldPersistPaymentWithAttributesContainingSponsorParty() throws Exception {
        byte[] requestBody = getTestResource("payment-with-attributes-with-sponsor.json");
        assertThat(repository.findAllPayments(), is(empty()));

        mockMvc.perform(post(PAYMENTS_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isCreated());
        FinancialTransaction transaction = getTheOnlyPersistedTransaction();
        FinancialTransactionAttributes attributes = transaction.getAttributes();
        TransactionParty sponsorParty = attributes.getSponsorParty();
        assertThat(sponsorParty.getAccountNumber(), is("56781234"));
        assertThat(sponsorParty.getBankId(), is("123123"));
        assertThat(sponsorParty.getBankIdCode(), is("GBDSC"));
    }

    @Test
    public void shouldPersistPaymentWithAttributesContainingFxInfo() throws Exception {
        byte[] requestBody = getTestResource("payment-with-attributes-with-fx.json");
        assertThat(repository.findAllPayments(), is(empty()));

        mockMvc.perform(post(PAYMENTS_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isCreated());
        FinancialTransaction transaction = getTheOnlyPersistedTransaction();
        FinancialTransactionAttributes attributes = transaction.getAttributes();
        ForeignExchangeInfo foreignExchangeInfo = attributes.getForeignExchangeInfo();
        assertThat(foreignExchangeInfo.getContractReference(), is("FX123"));
        assertThat(foreignExchangeInfo.getExchangeRate(), is(2.00000));
        assertThat(foreignExchangeInfo.getOriginalAmount(), is(200.42));
        assertThat(foreignExchangeInfo.getOriginalCurrency(), is("USD"));
    }

    @Test
    public void shouldPersistPaymentWithAttributesContainingChargesInfo() throws Exception {
        byte[] requestBody = getTestResource("payment-with-attributes-with-charges-information.json");
        assertThat(repository.findAllPayments(), is(empty()));

        mockMvc.perform(post(PAYMENTS_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isCreated());
        FinancialTransaction transaction = getTheOnlyPersistedTransaction();
        ChargesInformation chargesInformation = transaction.getAttributes().getChargesInformation();
        assertThat(chargesInformation.getBearerCode(), is(ChargeType.SHARED));
        assertThat(chargesInformation.getReceiverChargesAmount(), is(1.0));
        assertThat(chargesInformation.getReceiverChargesCurrency(), is("USD"));
        List<ChargeInfoForCurrency> senderCharges = chargesInformation.getSenderCharges();
        assertThat(senderCharges.size(), is(2));
        assertSenderChargeInfoForCurrency(senderCharges, "USD", 10.0);
        assertSenderChargeInfoForCurrency(senderCharges, "GBP", 5.0);
    }

    //
    // Helper methods.
    //

    private FinancialTransaction getTheOnlyPersistedTransaction() {
        List<FinancialTransaction> allPayments = repository.findAllPayments();
        assertThat(allPayments.size(), is(1));
        return allPayments.get(0);
    }
}
