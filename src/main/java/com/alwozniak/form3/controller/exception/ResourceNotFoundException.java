package com.alwozniak.form3.controller.exception;

/**
 * Exception notifying that requested resource was not found. Handled in PaymentsControllerAdvice class.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
