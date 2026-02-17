package com.weather.app.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Hidden
public class RootController {

    /**
     * Provides a discovery payload describing the application and its available routes.
     *
     * @return a Map containing:
     *         - "application": application name,
     *         - "version": application version,
     *         - "status": application status,
     *         - "endpoints": a nested Map that maps endpoint labels (e.g., "Swagger UI", "Cities API") to their URL paths
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Weather Service API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("endpoints", new HashMap<String, String>() {{
            put("Swagger UI", "/swagger-ui.html");
            put("OpenAPI JSON", "/api-docs");
            put("OpenAPI YAML", "/api-docs.yaml");
            put("Health", "/actuator/health");
            put("Cities API", "/api/cities");
            put("Weather API", "/api/weather");
        }});
        return ResponseEntity.ok(response);
    }

    /**
     * Provide a 404 Not Found response body listing error details and available endpoints.
     *
     * The response body is a map with keys:
     * - "error": short error title
     * - "message": human-readable explanation
     * - "statusCode": HTTP status code (404)
     * - "availableEndpoints": a map of named routes (Swagger UI, OpenAPI JSON, Cities API, Weather API, Health)
     *
     * @return a ResponseEntity with HTTP 404 and a body map containing error details and available endpoints
     */
    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> error() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Not Found");
        response.put("message", "The requested resource does not exist");
        response.put("statusCode", HttpStatus.NOT_FOUND.value());
        response.put("availableEndpoints", new HashMap<String, String>() {{
            put("Swagger UI", "/swagger-ui.html");
            put("OpenAPI JSON", "/api-docs");
            put("Cities API", "/api/cities");
            put("Weather API", "/api/weather");
            put("Health", "/actuator/health");
        }});
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}