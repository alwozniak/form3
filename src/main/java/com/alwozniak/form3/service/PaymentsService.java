package com.alwozniak.form3.service;

import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.repository.FinancialTransactionRepository;
import com.alwozniak.form3.resources.PaymentResourceData;
import com.alwozniak.form3.resources.PaymentsListResource;
import com.alwozniak.form3.resources.SinglePaymentResource;
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
        FinancialTransaction payment = financialTransactionRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment with id " + paymentId + " not found"));
        return new SinglePaymentResource(payment);
    }

    public FinancialTransaction createNewPaymentFromResource(PaymentResourceData paymentResourceData) {
        FinancialTransaction payment = FinancialTransaction.newPayment(paymentResourceData.getOrganisationId());
        return financialTransactionRepository.save(payment);
    }
}
