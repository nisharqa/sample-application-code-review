package com.weather.app.controller;

import com.weather.app.model.City;
import com.weather.app.service.CityService;
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
@RequestMapping("/api/cities")
@CrossOrigin(origins = "*")
@Tag(name = "Cities", description = "API endpoints for managing cities")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * Get all cities
     * GET /api/cities
     */
    @GetMapping
    @Operation(summary = "Get all cities", description = "Retrieve a list of all available cities")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of cities",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = City.class)))
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    /**
     * Get a city by ID
     * GET /api/cities/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get city by ID", description = "Retrieve a specific city by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved city",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    public ResponseEntity<City> getCityById(@Parameter(description = "City ID") @PathVariable Integer id) {
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Get a city by name
     * GET /api/cities/search?name=New York
     */
    @GetMapping("/search")
    @Operation(summary = "Search city by name", description = "Find a city by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found city",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    public ResponseEntity<City> getCityByName(@Parameter(description = "City name to search for") @RequestParam String name) {
        Optional<City> city = cityService.getCityByName(name);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new city
     * POST /api/cities
     */
    @PostMapping
    @Operation(summary = "Create a new city", description = "Add a new city to the system")
    @ApiResponse(responseCode = "201", description = "City successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = City.class)))
    public ResponseEntity<City> createCity(@Parameter(description = "City details") @RequestBody City city) {
        City newCity = cityService.addCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCity);
    }

    /**
     * Search cities using unsafe query - SQL Injection vulnerable endpoint
     * GET /api/cities/unsafe-search?filter=USA
     */
    @GetMapping("/unsafe-search")
    @Operation(summary = "Search cities (UNSAFE)", description = "Search cities by name or country (vulnerable to SQL injection)")
    public ResponseEntity<List<City>> unsafeSearchCities(@RequestParam String filter) {
        List<City> results = cityService.searchCitiesUnsafe(filter);
        return ResponseEntity.ok(results);
    }

    /**
     * Create city without validation - Missing Validation
     * POST /api/cities/unsafe
     */
    @PostMapping("/unsafe")
    @Operation(summary = "Create city without validation", description = "Add city without proper input validation")
    public ResponseEntity<City> createCityWithoutValidation(@RequestBody City city) {
        // No validation on city object - could be null, have empty strings, etc.
        City newCity = cityService.addCityWithoutValidation(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCity);
    }

    /**
     * Issue: Null Pointer Exception - No null check on request parameter
     * GET /api/cities/details?name=New York
     */
    @GetMapping("/details")
    @Operation(summary = "Get city details", description = "Get detailed city info without null checks")
    public ResponseEntity<String> getCityDetails(@RequestParam String name) {
        // Issue: No null check - name.toUpperCase() will throw NPE if name is null
        String upperName = name.toUpperCase();
        String details = "City: " + name.trim() + ", Length: " + name.length();
        return ResponseEntity.ok(details);
    }

    /**
     * Issue: Potential NPE - Optional not handled properly
     * GET /api/cities/{id}/info
     */
    @GetMapping("/{id}/info")
    @Operation(summary = "Get city info", description = "Get city info with poor null handling")
    public ResponseEntity<String> getCityInfo(@PathVariable Integer id) {
        Optional<City> city = cityService.getCityById(id);
        // Issue: Direct .get() on Optional without checking if present
        City foundCity = city.get(); // Will throw NoSuchElementException if not found
        String info = foundCity.getName() + " is located in " + foundCity.getCountry();
        return ResponseEntity.ok(info);
    }

    /**
     * Issue: NPE from service layer - No defensive programming
     * GET /api/cities/country/{country}
     */
    @GetMapping("/country/{country}")
    @Operation(summary = "Get cities by country", description = "Find all cities in a country")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable String country) {
        List<City> cities = cityService.getCitiesByCountry(country);
        // Issue: No null check - cities could be null from service layer
        int count = cities.size(); // NPE if cities is null
        return ResponseEntity.ok(cities);
    }
}
