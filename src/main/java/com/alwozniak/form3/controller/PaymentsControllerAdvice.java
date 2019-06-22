package com.alwozniak.form3.controller;

import com.alwozniak.form3.controller.exception.ResourceNotFoundException;
import com.alwozniak.form3.resources.ExceptionResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class PaymentsControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionResource handleResourceNotFound(final ResourceNotFoundException exception) {
        return new ExceptionResource(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResource handlePaymentIdConversionError() {
        return new ExceptionResource("Bad request data.");
    }
}
