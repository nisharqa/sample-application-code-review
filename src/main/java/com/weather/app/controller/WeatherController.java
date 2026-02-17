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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
@Tag(name = "Weather", description = "API endpoints for managing weather information")
public class WeatherController {

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
}
