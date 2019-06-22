package com.alwozniak.form3.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resource returned when exception occurs, eg. when requested resource is not found.
 */
public class ExceptionResource {

    @JsonProperty("errorMessage")
    private String message;

    public ExceptionResource(String message) {
        this.message = message;
    }
}
