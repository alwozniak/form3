package com.alwozniak.form3.controller;

import com.alwozniak.form3.controller.exception.ResourceNotFoundException;
import com.alwozniak.form3.resources.PaymentsResource;
import com.alwozniak.form3.service.PaymentsService;
import com.alwozniak.form3.service.exception.PaymentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/payments", produces = "application/json")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @GetMapping
    public PaymentsResource getPayments() {
        return paymentsService.getPaymentsListResource();
    }

    @GetMapping("/{paymentId}")
    public PaymentsResource getSinglePayment(@PathVariable("paymentId") UUID paymentId) {
        try {
            return paymentsService.getSinglePaymentResource(paymentId);
        } catch (PaymentNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
