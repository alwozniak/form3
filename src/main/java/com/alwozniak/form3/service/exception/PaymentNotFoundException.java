package com.alwozniak.form3.service.exception;

import java.util.UUID;

public class PaymentNotFoundException extends Exception {
    public PaymentNotFoundException(UUID paymentId) {
        super("Payment with id " + paymentId + " not found.");
    }
}
