package com.weather.app.controller;

import com.weather.app.model.City;
import com.weather.app.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "*")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * Get all cities
     * GET /api/cities
     */
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    /**
     * Get a city by ID
     * GET /api/cities/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Integer id) {
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Get a city by name
     * GET /api/cities/search?name=New York
     */
    @GetMapping("/search")
    public ResponseEntity<City> getCityByName(@RequestParam String name) {
        Optional<City> city = cityService.getCityByName(name);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new city
     * POST /api/cities
     */
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City newCity = cityService.addCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCity);
    }
}
