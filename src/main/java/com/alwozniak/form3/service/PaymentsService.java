package com.alwozniak.form3.service;

import com.alwozniak.form3.domain.*;
import com.alwozniak.form3.domain.AccountData.AccountNumberCode;
import com.alwozniak.form3.repository.FinancialTransactionRepository;
import com.alwozniak.form3.resources.*;
import com.alwozniak.form3.service.exception.PaymentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentsService {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    public PaymentsListResource getPaymentsListResource() {
        List<FinancialTransaction> payments = financialTransactionRepository.findAllPayments();
        return new PaymentsListResource(payments);
    }

    public SinglePaymentResource getSinglePaymentResource(UUID paymentId) throws PaymentNotFoundException {
        FinancialTransaction payment = financialTransactionRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId ));
        return new SinglePaymentResource(payment);
    }

    public FinancialTransaction createNewPaymentFromResource(PaymentResourceData paymentResourceData) {
        FinancialTransaction payment = FinancialTransaction.newPayment(paymentResourceData.getOrganisationId());
        PaymentAttributesResource paymentAttributesResource = paymentResourceData.getPaymentAttributesResource();
        if (paymentAttributesResource != null) {
            FinancialTransactionAttributes attributes = createAttributesFromResource(paymentAttributesResource, payment);
            payment.setAttributes(attributes);
        }
        return financialTransactionRepository.save(payment);
    }

    public FinancialTransaction updatePayment(UUID paymentId, PaymentResourceData paymentResourceData)
            throws PaymentNotFoundException {
        return financialTransactionRepository.findById(paymentId)
                .map(payment -> {
                    payment.updateFields(paymentResourceData.getOrganisationId(),
                            paymentResourceData.getTransactionType(),
                            paymentResourceData.getVersion());
                    PaymentAttributesResource attributesResource = paymentResourceData.getPaymentAttributesResource();
                    if (attributesResource != null) {
                        FinancialTransactionAttributes paymentAttributes = payment.getAttributes();
                        if (paymentAttributes == null) {
                            payment.setAttributes(createAttributesFromResource(attributesResource, payment));
                        } else {
                            paymentAttributes.updateFields(attributesResource.getAmount(),
                                    attributesResource.getCurrency(), attributesResource.getEndToEndReference(),
                                    attributesResource.getNumericReference(), attributesResource.getPaymentId(),
                                    attributesResource.getPaymentPurpose(), attributesResource.getPaymentType(),
                                    attributesResource.getPaymentScheme(), attributesResource.getProcessingDate(),
                                    attributesResource.getReference(), attributesResource.getSchemePaymentType(),
                                    attributesResource.getSchemePaymentSubType());
                        }
                    }
                    return financialTransactionRepository.save(payment);
                })
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    private FinancialTransactionAttributes createAttributesFromResource(PaymentAttributesResource attributesResource,
                                                                        FinancialTransaction paymentTransaction) {
        FinancialTransactionAttributes.Builder attributesBuilder = FinancialTransactionAttributes.builder(paymentTransaction)
                .withAmountInCurrency(attributesResource.getAmount(), attributesResource.getCurrency())
                .withReference(attributesResource.getReference())
                .withProcessingDate(attributesResource.getProcessingDate())
                .withPaymentSchemeData(attributesResource.getPaymentScheme(), attributesResource.getSchemePaymentType(),
                        attributesResource.getSchemePaymentSubType())
                .withPaymentData(attributesResource.getPaymentId(), attributesResource.getPaymentPurpose(),
                        attributesResource.getPaymentType())
                .withNumericReference(attributesResource.getNumericReference())
                .withEndToEndReference(attributesResource.getEndToEndReference());

        TransactionPartyResource beneficiaryResource = attributesResource.getBeneficiaryPartyResource();
        if (beneficiaryResource != null) {
            attributesBuilder.withBeneficiary(createTransactionPartyFromResource(beneficiaryResource));
        }

        TransactionPartyResource debtorResource = attributesResource.getDebtorPartyResource();
        if (debtorResource != null) {
            attributesBuilder.withDebtor(createTransactionPartyFromResource(debtorResource));
        }

        TransactionPartyResource sponsorResource = attributesResource.getSponsorPartyResource();
        if (sponsorResource != null) {
            attributesBuilder.withSponsorParty(createTransactionPartyFromResource(sponsorResource));
        }

        ForeignExchangeInfoResource fxInfoResource = attributesResource.getForeignExchangeInfoResource();
        if (fxInfoResource != null) {
            attributesBuilder.withForeignExchangeInfo(
                    new ForeignExchangeInfo(fxInfoResource.getContractReference(), fxInfoResource.getExchangeRate(),
                            fxInfoResource.getOriginalAmount(), fxInfoResource.getOriginalCurrency()));
        }

        ChargesInformationResource chargesInformationResource = attributesResource.getChargesInformationResource();
        if (chargesInformationResource != null) {
            ChargesInformation.Builder chargesInformationBuilder =
                    ChargesInformation.builder(chargesInformationResource.getBearerCode())
                            .withReceiverCharge(chargesInformationResource.getReceiverChargesAmount(),
                                    chargesInformationResource.getReceiverChargesCurrency());
            chargesInformationResource.getSenderChargeResources()
                    .forEach(resource ->
                            chargesInformationBuilder.withSenderCharge(resource.getAmount(), resource.getCurrency()));
            attributesBuilder.withChargesInformation(chargesInformationBuilder.build());
        }

        return attributesBuilder.build();
    }

    private TransactionParty createTransactionPartyFromResource(TransactionPartyResource resource) {
        return TransactionParty.builder()
                .withAccountData(createAccountData(resource))
                .withBankIdData(resource.getBankId(), resource.getBankIdCode())
                .withAddress(resource.getAddress())
                .withName(resource.getName())
                .build();
    }

    private AccountData createAccountData(TransactionPartyResource resource) {
        if (resource.getAccountNumberCode() == null) {
            return new AccountData(resource.getAccountNumber());
        }

        AccountData.Builder accountDataBuilder = AccountData.builder(resource.getAccountName());
        AccountNumberCode accountNumberCode = resource.getAccountNumberCode();
        if (accountNumberCode == AccountNumberCode.BBAN) {
            accountDataBuilder.withBbanNumber(resource.getAccountNumber());
        } else if (accountNumberCode == AccountNumberCode.IBAN) {
            accountDataBuilder.withIbanNumber(resource.getAccountNumber());
        }
        Integer accountType = resource.getAccountType();
        if (accountType != null) {
            accountDataBuilder.withType(accountType);
        }
        return accountDataBuilder.build();
    }
}
