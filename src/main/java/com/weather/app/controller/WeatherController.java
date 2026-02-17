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
     * Retrieve weather information for all cities.
     *
     * @return a ResponseEntity containing a list of Weather objects with HTTP status 200
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
     * Retrieve weather information for a city identified by its ID.
     *
     * @param cityId the ID of the city to retrieve weather for
     * @return 200 with the Weather when found, 404 when no weather data exists for the given city ID
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
     * Retrieve weather information for a city by name.
     *
     * @param cityName the city name to search for
     * @return a ResponseEntity containing the city's Weather with HTTP 200 when found, or HTTP 404 when not found
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
     * Update the stored weather for the specified city.
     *
     * @param cityId the ID of the city whose weather should be updated
     * @param weather the updated weather data to persist for the city
     * @return ResponseEntity containing the updated Weather with HTTP status 200 if the city exists; HTTP status 404 with an empty body if the city is not found
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
     * Create weather information for a city.
     *
     * @param weather the weather details to persist
     * @return the persisted Weather object including any generated fields
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