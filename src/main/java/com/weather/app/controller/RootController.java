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
