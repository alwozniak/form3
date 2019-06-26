package com.alwozniak.form3.controller;

import com.alwozniak.form3.controller.exception.ResourceNotFoundException;
import com.alwozniak.form3.domain.FinancialTransaction;
import com.alwozniak.form3.resources.PaymentResourceData;
import com.alwozniak.form3.resources.PaymentsResource;
import com.alwozniak.form3.service.PaymentsService;
import com.alwozniak.form3.service.exception.PaymentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> postNewPayment(@RequestBody PaymentResourceData paymentResourceData) {
        FinancialTransaction payment = paymentsService.createNewPaymentFromResource(paymentResourceData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{paymentId}").buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
