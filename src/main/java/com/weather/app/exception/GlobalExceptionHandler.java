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

    /**
     * Produce a 404 Not Found response describing the missing resource and listing common application endpoints.
     *
     * @param ex the NoHandlerFoundException containing the original request URL
     * @return a ResponseEntity whose body is a Map with keys:
     *         "timestamp" (LocalDateTime), "status" (int 404), "error" (String "Not Found"),
     *         "message" (String describing the missing resource), "path" (requested URL),
     *         and "availableEndpoints" (Map<String,String> of common application URLs)
     */
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

    /**
     * Handle uncaught exceptions and produce a standardized HTTP 500 error response.
     *
     * The response body is a Map containing `timestamp`, `status`, `error`, and `message`.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity whose body is a map with keys:
     *         - "timestamp": the time the error was handled
     *         - "status": 500
     *         - "error": "Internal Server Error"
     *         - "message": the exception's message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}