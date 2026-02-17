package com.weather.app.controller;

import com.weather.app.model.Weather;
import com.weather.app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * Get all weather data
     * GET /api/weather
     */
    @GetMapping
    public ResponseEntity<List<Weather>> getAllWeather() {
        List<Weather> weatherList = weatherService.getAllWeather();
        return ResponseEntity.ok(weatherList);
    }

    /**
     * Get weather for a specific city by city ID
     * GET /api/weather/city/{cityId}
     */
    @GetMapping("/city/{cityId}")
    public ResponseEntity<Weather> getWeatherByCityId(@PathVariable Integer cityId) {
        Optional<Weather> weather = weatherService.getWeatherByCityId(cityId);
        return weather.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Get weather for a specific city by city name
     * GET /api/weather/search?cityName=New York
     */
    @GetMapping("/search")
    public ResponseEntity<Weather> getWeatherByCityName(@RequestParam String cityName) {
        Optional<Weather> weather = weatherService.getWeatherByCityName(cityName);
        return weather.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Update weather for a city
     * PUT /api/weather/city/{cityId}
     */
    @PutMapping("/city/{cityId}")
    public ResponseEntity<Weather> updateWeather(@PathVariable Integer cityId,
                                                  @RequestBody Weather weather) {
        Optional<Weather> updatedWeather = weatherService.updateWeather(cityId, weather);
        return updatedWeather.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create new weather data for a city
     * POST /api/weather
     */
    @PostMapping
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) {
        Weather newWeather = weatherService.addWeather(weather);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWeather);
    }
}
