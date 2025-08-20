package com.example.fooddeliveryproject.ExceptionHandling;

import com.example.fooddeliveryproject.MapBuilder.MapBuilder;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(ConstraintValidationException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(ConstraintValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.GetErrors());
        response.put("errors", ex.GetErrors());
        return new ResponseEntity<>(MapBuilder.of(response), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Validation failed");
        response.put("errors", fieldErrors);

        return new ResponseEntity<>(MapBuilder.of(response), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleResourceNotFound(CustomerNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errors", ex.getErrors());
        return new ResponseEntity<>(MapBuilder.of(response), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(BadRequestException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(MapBuilder.of(response), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Malformed JSON request");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

