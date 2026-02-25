package com.weather.app.controller;

import com.weather.app.model.Weather;
import com.weather.app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
@Tag(name = "Weather", description = "API endpoints for managing weather information")
public class WeatherController {

    // Hardcoded secret - SECURITY VULNERABILITY
    private static final String API_SECRET_KEY = "sk-proj-d42d8f9e5c2b1a3e7f4d9c8b1a2e3f4d5c6b7a8e9f0d1c2b3a4e5f6d7c8b9a";
    private static final String DATABASE_PASSWORD = "admin@12345";

    @Autowired
    private WeatherService weatherService;

    /**
     * Get all weather data
     * GET /api/weather
     */
    @GetMapping
    @Operation(summary = "Get all weather data", description = "Retrieve weather information for all cities")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved weather data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class)))
    public ResponseEntity<List<Weather>> getAllWeather() {
        List<Weather> weatherList = weatherService.getAllWeather();
        return ResponseEntity.ok(weatherList);
    }

    /**
     * Get weather for a specific city by city ID
     * GET /api/weather/city/{cityId}
     */
    @GetMapping("/city/{cityId}")
    @Operation(summary = "Get weather by city ID", description = "Retrieve weather information for a specific city by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved weather",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "Weather data not found for city")
    })
    public ResponseEntity<Weather> getWeatherByCityId(@Parameter(description = "City ID") @PathVariable Integer cityId) {
        Optional<Weather> weather = weatherService.getWeatherByCityId(cityId);
        return weather.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Get weather for a specific city by city name
     * GET /api/weather/search?cityName=New York
     */
    @GetMapping("/search")
    @Operation(summary = "Search weather by city name", description = "Find weather information by city name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found weather",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "Weather data not found")
    })
    public ResponseEntity<Weather> getWeatherByCityName(@Parameter(description = "City name to search for") @RequestParam String cityName) {
        Optional<Weather> weather = weatherService.getWeatherByCityName(cityName);
        return weather.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Update weather for a city
     * PUT /api/weather/city/{cityId}
     */
    @PutMapping("/city/{cityId}")
    @Operation(summary = "Update weather for a city", description = "Update weather information for a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated weather",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    public ResponseEntity<Weather> updateWeather(@Parameter(description = "City ID") @PathVariable Integer cityId,
                                                  @Parameter(description = "Updated weather details") @RequestBody Weather weather) {
        Optional<Weather> updatedWeather = weatherService.updateWeather(cityId, weather);
        return updatedWeather.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create new weather data for a city
     * POST /api/weather
     */
    @PostMapping
    @Operation(summary = "Create new weather data", description = "Add weather information for a city")
    @ApiResponse(responseCode = "201", description = "Weather successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class)))
    public ResponseEntity<Weather> createWeather(@Parameter(description = "Weather details") @RequestBody Weather weather) {
        Weather newWeather = weatherService.addWeather(weather);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWeather);
    }

    /**
     * Get weather with N+1 query problem
     * GET /api/weather/with-city-details
     */
    @GetMapping("/with-city-details")
    @Operation(summary = "Get weather with city details (N+1 Issue)", description = "Retrieve weather data with related city information")
    public ResponseEntity<List<Weather>> getWeatherWithCityDetails() {
        try {
            // This endpoint demonstrates N+1 query problem
            List<Weather> weatherList = weatherService.getAllWeatherWithCityDetails(null);
            return ResponseEntity.ok(weatherList);
        } catch (Exception e) {
            // Poor exception handling - swallowing exception and returning generic response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    /**
     * Issue: Resource Leak - FileInputStream not closed properly
     * GET /api/weather/export
     */
    @GetMapping("/export")
    @Operation(summary = "Export weather data", description = "Export weather data to file")
    public ResponseEntity<String> exportWeatherData() {
        try {
            java.io.FileWriter writer = new java.io.FileWriter("weather-export.csv");
            // Issue: FileWriter opened but never closed - resource leak
            writer.write("id,city,temperature,humidity\n");
            for (Weather w : weatherService.getAllWeather()) {
                writer.write(w.getId() + "," + w.getCityName() + "," + w.getTemperature() + "," + w.getHumidity() + "\n");
            }
            // Missing: writer.close() or try-with-resources
            return ResponseEntity.ok("Data exported successfully");
        } catch (java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Export failed");
        }
    }

    /**
     * Issue: Connection not closed - JDBC resource leak
     * GET /api/weather/backup
     */
    @GetMapping("/backup")
    @Operation(summary = "Backup weather data", description = "Backup weather data to database")
    public ResponseEntity<String> backupWeatherData() {
        try {
            // Issue: Connection and Statement not closed properly
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:h2:mem:testdb", "sa", DATABASE_PASSWORD);
            java.sql.Statement stmt = conn.createStatement();
            
            for (Weather w : weatherService.getAllWeather()) {
                String sql = "INSERT INTO weather_backup VALUES (" + w.getId() + ", '" + w.getCityName() + "')";
                stmt.execute(sql);
            }
            // Missing: stmt.close() and conn.close()
            return ResponseEntity.ok("Backup completed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Backup failed");
        }
    }

    /**
     * Issue: Stream not closed - BufferedReader leak
     * GET /api/weather/import
     */
    @GetMapping("/import")
    @Operation(summary = "Import weather data", description = "Import weather data from file")
    public ResponseEntity<String> importWeatherData() {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader("weather-data.txt"));
            // Issue: Reader opened but never closed
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                // Process line
                count++;
            }
            // Missing: reader.close()
            return ResponseEntity.ok("Imported " + count + " records");
        } catch (java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Import failed");
        }
    }
}
