package com.alwozniak.form3.service;

import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.domain.FinancialTransaction.FinancialTransactionType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentScheme;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.PaymentType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentSubType;
import com.alwozniak.form3.domain.FinancialTransactionAttributes.SchemePaymentType;
import com.alwozniak.form3.domain.TransactionParty;
import com.alwozniak.form3.repository.FinancialTransactionRepository;
import com.alwozniak.form3.resources.*;
import com.alwozniak.form3.service.exception.PaymentNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    @Test
    public void shouldCreateNewPaymentFromPaymentResourceDataWithAttributes() throws ParseException {
        PaymentResourceData paymentResourceData = new PaymentResourceData();
        paymentResourceData.setTypeFromString("Payment");
        paymentResourceData.setVersion(0);
        paymentResourceData.setOrganisationIdFromString(UUID.randomUUID().toString());
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();
        paymentAttributesResource.setAmountFromString("120.99");
        String currency = "PLN";
        paymentAttributesResource.setCurrency(currency);
        String endToEndReference = "End to end reference string.";
        paymentAttributesResource.setEndToEndReference(endToEndReference);
        String numericReference = "1002001";
        paymentAttributesResource.setNumericReference(numericReference);
        String paymentId = "123456789012345678";
        paymentAttributesResource.setPaymentId(paymentId);
        paymentAttributesResource.setPaymentSchemeFromString("FPS");
        String paymentPurpose = "This is payment purpose string.";
        paymentAttributesResource.setPaymentPurpose(paymentPurpose);
        LocalDate localDate = LocalDate.of(2019, 4, 12);
        Date processingDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        paymentAttributesResource.setProcessingDateFromString("2019-04-12");
        String paymentReference = "Payment reference.";
        paymentAttributesResource.setReference(paymentReference);
        paymentAttributesResource.setPaymentTypeFromString("Credit");
        paymentAttributesResource.setSchemePaymentTypeFromString("ImmediatePayment");
        paymentAttributesResource.setSchemePaymentSubTypeFromString("InternetBanking");
        paymentResourceData.setPaymentAttributesResource(paymentAttributesResource);

        FinancialTransaction payment = paymentsService.createNewPaymentFromResource(paymentResourceData);

        FinancialTransactionAttributes paymentAttributes = payment.getAttributes();
        assertThat(paymentAttributes.getAmount(), is(120.99));
        assertThat(paymentAttributes.getCurrency(), is(currency));
        assertThat(paymentAttributes.getEndToEndReference(), is(endToEndReference));
        assertThat(paymentAttributes.getNumericReference(), is(numericReference));
        assertThat(paymentAttributes.getPaymentId(), is(paymentId));
        assertThat(paymentAttributes.getPaymentScheme(), is(PaymentScheme.FPS));
        assertThat(paymentAttributes.getPaymentPurpose(), is(paymentPurpose));
        assertThat(paymentAttributes.getProcessingDate(), is(processingDate));
        assertThat(paymentAttributesResource.getReference(), is(paymentReference));
        assertThat(paymentAttributesResource.getPaymentType(), is(PaymentType.CREDIT));
        assertThat(paymentAttributesResource.getSchemePaymentType(), is(SchemePaymentType.IMMEDIATE_PAYMENT));
        assertThat(paymentAttributesResource.getSchemePaymentSubType(), is(SchemePaymentSubType.INTERNET_BANKING));
    }

    @Test
    public void shouldCreateNewPaymentWithAttributesContainingBeneficiaryParty() {
        String accountName = "W Owens";
        String accountNumber = "31926819";
        String address = "1 The Beneficiary Localtown SE2";
        String bankId = "403000";
        String bankIdCode = "GBDSC";
        String name = "Wilfred Jeremiah Owens";
        PaymentResourceData paymentResourceData = new PaymentResourceData();
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();
        TransactionPartyResource beneficiaryPartyResource = new TransactionPartyResource();
        beneficiaryPartyResource.setAccountName(accountName);
        beneficiaryPartyResource.setAccountNumber(accountNumber);
        beneficiaryPartyResource.setAccountNumberCode("BBAN");
        beneficiaryPartyResource.setAccountType(0);
        beneficiaryPartyResource.setAddress(address);
        beneficiaryPartyResource.setBankId(bankId);
        beneficiaryPartyResource.setBankIdCode(bankIdCode);
        beneficiaryPartyResource.setName(name);
        paymentAttributesResource.setBeneficiaryPartyResource(beneficiaryPartyResource);
        paymentResourceData.setPaymentAttributesResource(paymentAttributesResource);

        FinancialTransaction payment = paymentsService.createNewPaymentFromResource(paymentResourceData);

        TransactionParty beneficiaryParty = payment.getAttributes().getBeneficiaryParty();
        assertThat(beneficiaryParty.getAccountName(), is(accountName));
        assertThat(beneficiaryParty.getAccountNumber(), is(accountNumber));
        assertThat(beneficiaryParty.getAccountNumberCode(), is(AccountNumberCode.BBAN));
        assertThat(beneficiaryParty.getAccountType(), is(0));
        assertThat(beneficiaryParty.getAddress(), is(address));
        assertThat(beneficiaryParty.getBankId(), is(bankId));
        assertThat(beneficiaryParty.getBankIdCode(), is(bankIdCode));
        assertThat(beneficiaryParty.getName(), is(name));
    }

    @Test
    public void shouldCreateNewPaymentWithAttributesContainingDebtorParty() {
        String accountName = "EJ Brown Black";
        String accountNumber = "GB29XABC10161234567801";
        String address = "10 Debtor Crescent Sourcetown NE1";
        String bankId = "203301";
        String bankIdCode = "GBDSC";
        String name = "Emelia Jane Brown";
        PaymentResourceData paymentResourceData = new PaymentResourceData();
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();
        TransactionPartyResource debtorPartyResource = new TransactionPartyResource();
        debtorPartyResource.setAccountName(accountName);
        debtorPartyResource.setAccountNumber(accountNumber);
        debtorPartyResource.setAccountNumberCode("IBAN");
        debtorPartyResource.setAddress(address);
        debtorPartyResource.setBankId(bankId);
        debtorPartyResource.setBankIdCode(bankIdCode);
        debtorPartyResource.setName(name);
        paymentAttributesResource.setDebtorPartyResource(debtorPartyResource);
        paymentResourceData.setPaymentAttributesResource(paymentAttributesResource);

        FinancialTransaction payment = paymentsService.createNewPaymentFromResource(paymentResourceData);

        TransactionParty debtorParty = payment.getAttributes().getDebtorParty();
        assertThat(debtorParty.getAccountName(), is(accountName));
        assertThat(debtorParty.getAccountNumber(), is(accountNumber));
        assertThat(debtorParty.getAccountNumberCode(), is(AccountNumberCode.IBAN));
        assertThat(debtorParty.getAddress(), is(address));
        assertThat(debtorParty.getBankId(), is(bankId));
        assertThat(debtorParty.getBankIdCode(), is(bankIdCode));
        assertThat(debtorParty.getName(), is(name));
    }

    @Test
    public void shouldCreateNewPaymentWithAttributesContainingSponsorParty() {
        String accountNumber = "GB29XABC10161234567801";
        String bankId = "203301";
        String bankIdCode = "GBDSC";
        PaymentResourceData paymentResourceData = new PaymentResourceData();
        PaymentAttributesResource paymentAttributesResource = new PaymentAttributesResource();
        TransactionPartyResource sponsorPartyResource = new TransactionPartyResource();
        sponsorPartyResource.setAccountNumber(accountNumber);
        sponsorPartyResource.setBankId(bankId);
        sponsorPartyResource.setBankIdCode(bankIdCode);
        paymentAttributesResource.setSponsorPartyResource(sponsorPartyResource);
        paymentResourceData.setPaymentAttributesResource(paymentAttributesResource);

        FinancialTransaction payment = paymentsService.createNewPaymentFromResource(paymentResourceData);

        TransactionParty debtorParty = payment.getAttributes().getSponsorParty();
        assertThat(debtorParty.getAccountNumber(), is(accountNumber));
        assertThat(debtorParty.getBankId(), is(bankId));
        assertThat(debtorParty.getBankIdCode(), is(bankIdCode));
    }
}
