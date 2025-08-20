package com.example.fooddeliveryproject.ExceptionHandling;

import java.util.Map;

public class CustomerNotFoundException extends RuntimeException {
    private final Map<String, String> errors;
    public CustomerNotFoundException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
    public CustomerNotFoundException(String field, String message) {
        super(message);
        this.errors = Map.of(field, message);
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}
