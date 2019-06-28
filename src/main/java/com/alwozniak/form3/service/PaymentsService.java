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
import java.util.function.BiConsumer;

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
            payment.setAttributes(createAttributesFromResource(paymentAttributesResource, payment));
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
                            createOrUpdateTransactionParty(paymentAttributes,
                                    paymentAttributes.getBeneficiaryParty(), attributesResource.getBeneficiaryPartyResource(),
                                    FinancialTransactionAttributes::setBeneficiaryParty);
                            createOrUpdateTransactionParty(paymentAttributes,
                                    paymentAttributes.getDebtorParty(), attributesResource.getDebtorPartyResource(),
                                    FinancialTransactionAttributes::setDebtorParty);
                            createOrUpdateTransactionParty(paymentAttributes,
                                    paymentAttributes.getSponsorParty(), attributesResource.getSponsorPartyResource(),
                                    FinancialTransactionAttributes::setSponsorParty);
                            createOrUpdateForeignExchangeInfo(paymentAttributes,
                                    attributesResource.getForeignExchangeInfoResource());
                        }
                    }
                    return financialTransactionRepository.save(payment);
                })
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    private void createOrUpdateForeignExchangeInfo(FinancialTransactionAttributes paymentAttributes,
                                                   ForeignExchangeInfoResource resource) {
        if (resource != null) {
            ForeignExchangeInfo foreignExchangeInfo = paymentAttributes.getForeignExchangeInfo();
            if (foreignExchangeInfo == null) {
                paymentAttributes.setForeignExchangeInfo(createForeignExchangeInfoFromResource(resource));
            } else {
                foreignExchangeInfo.updateFields(resource.getContractReference(), resource.getExchangeRate(),
                        resource.getOriginalCurrency(), resource.getOriginalAmount());
            }
        }
    }

    private void createOrUpdateTransactionParty(FinancialTransactionAttributes attributes,
                                                TransactionParty partyToUpdate, TransactionPartyResource partyResource,
                                                BiConsumer<FinancialTransactionAttributes, TransactionParty> transactionPartySetter) {
        if (partyResource != null) {
            if (partyToUpdate == null) {
                transactionPartySetter.accept(attributes, createTransactionPartyFromResource(partyResource));
            } else {
                partyToUpdate.updateFields(partyResource.getAccountNumberCode(), partyResource.getAccountNumber(),
                        partyResource.getAccountType(), partyResource.getAccountName(), partyResource.getAddress(),
                        partyResource.getBankId(), partyResource.getBankIdCode(), partyResource.getName());
            }
        }
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

        maybeAddBeneficiary(attributesResource, attributesBuilder);
        maybeAddDebtor(attributesResource, attributesBuilder);
        maybeAddSponsor(attributesResource, attributesBuilder);
        maybeAddForeignExchangeInfo(attributesResource, attributesBuilder);
        maybeAddChargesInformation(attributesResource, attributesBuilder);

        return attributesBuilder.build();
    }

    private void maybeAddChargesInformation(PaymentAttributesResource attributesResource,
                                            FinancialTransactionAttributes.Builder attributesBuilder) {
        ChargesInformationResource chargesInformationResource = attributesResource.getChargesInformationResource();
        if (chargesInformationResource != null) {
            attributesBuilder.withChargesInformation(createChargesInformationFromResource(chargesInformationResource));
        }
    }

    private void maybeAddForeignExchangeInfo(PaymentAttributesResource attributesResource,
                                             FinancialTransactionAttributes.Builder attributesBuilder) {
        ForeignExchangeInfoResource fxInfoResource = attributesResource.getForeignExchangeInfoResource();
        if (fxInfoResource != null) {
            attributesBuilder.withForeignExchangeInfo(createForeignExchangeInfoFromResource(fxInfoResource));
        }
    }

    private void maybeAddSponsor(PaymentAttributesResource attributesResource,
                                 FinancialTransactionAttributes.Builder attributesBuilder) {
        TransactionPartyResource sponsorResource = attributesResource.getSponsorPartyResource();
        if (sponsorResource != null) {
            attributesBuilder.withSponsorParty(createTransactionPartyFromResource(sponsorResource));
        }
    }

    private void maybeAddDebtor(PaymentAttributesResource attributesResource,
                                FinancialTransactionAttributes.Builder attributesBuilder) {
        TransactionPartyResource debtorResource = attributesResource.getDebtorPartyResource();
        if (debtorResource != null) {
            attributesBuilder.withDebtor(createTransactionPartyFromResource(debtorResource));
        }
    }

    private void maybeAddBeneficiary(PaymentAttributesResource attributesResource,
                                     FinancialTransactionAttributes.Builder attributesBuilder) {
        TransactionPartyResource beneficiaryResource = attributesResource.getBeneficiaryPartyResource();
        if (beneficiaryResource != null) {
            attributesBuilder.withBeneficiary(createTransactionPartyFromResource(beneficiaryResource));
        }
    }

    private ChargesInformation createChargesInformationFromResource(ChargesInformationResource chargesInformationResource) {
        ChargesInformation.Builder chargesInformationBuilder =
                ChargesInformation.builder(chargesInformationResource.getBearerCode())
                        .withReceiverCharge(chargesInformationResource.getReceiverChargesAmount(),
                                chargesInformationResource.getReceiverChargesCurrency());
        chargesInformationResource.getSenderChargeResources()
                .forEach(resource ->
                        chargesInformationBuilder.withSenderCharge(resource.getAmount(), resource.getCurrency()));
        return chargesInformationBuilder.build();
    }

    private ForeignExchangeInfo createForeignExchangeInfoFromResource(ForeignExchangeInfoResource fxInfoResource) {
        return new ForeignExchangeInfo(fxInfoResource.getContractReference(), fxInfoResource.getExchangeRate(),
                fxInfoResource.getOriginalAmount(), fxInfoResource.getOriginalCurrency());
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
