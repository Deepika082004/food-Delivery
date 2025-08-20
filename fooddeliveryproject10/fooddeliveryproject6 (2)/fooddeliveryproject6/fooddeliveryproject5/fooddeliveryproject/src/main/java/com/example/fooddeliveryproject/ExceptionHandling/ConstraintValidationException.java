package com.example.fooddeliveryproject.ExceptionHandling;

import java.util.Map;

public class ConstraintValidationException extends RuntimeException {
    private final Map<String, String> errors;


    public ConstraintValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public ConstraintValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
    public ConstraintValidationException(String field, String message) {
        super(message);
        this.errors = Map.of(field, message);
    }
    public Map<String, String> GetErrors() {
        return errors;
    }
}
