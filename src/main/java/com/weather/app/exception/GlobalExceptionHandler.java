package com.weather.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFound(NoHandlerFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", "The requested resource does not exist: " + ex.getRequestURL());
        response.put("path", ex.getRequestURL());
        response.put("availableEndpoints", new HashMap<String, String>() {{
            put("Home", "http://localhost:8080/");
            put("Swagger UI", "http://localhost:8080/swagger-ui.html");
            put("OpenAPI JSON", "http://localhost:8080/api-docs");
            put("OpenAPI YAML", "http://localhost:8080/api-docs.yaml");
            put("Cities API", "http://localhost:8080/api/cities");
            put("Weather API", "http://localhost:8080/api/weather");
            put("Health Check", "http://localhost:8080/actuator/health");
        }});
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        
        // Poor exception handling - exposing sensitive stack trace information
        response.put("cause", ex.getCause());
        response.put("stackTrace", ex.getStackTrace());
        
        // Suppressed exceptions - poor error context
        if (ex.getSuppressed() != null && ex.getSuppressed().length > 0) {
            response.put("suppressedExceptions", ex.getSuppressed()[0].getMessage());
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Unsafe exception handler that swallows exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Poor exception handling - not logging, not providing feedback, silently failing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Swallowing exception
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
