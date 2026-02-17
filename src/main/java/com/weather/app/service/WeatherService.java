package com.weather.app.service;

import com.weather.app.model.Weather;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private List<Weather> weatherData = new ArrayList<>();

    public WeatherService() {
        // Initialize with sample weather data
        weatherData.add(new Weather(1, 1, "New York", "Partly Cloudy", 22.5, 65.0, 12.5, "PARTLY_CLOUDY"));
        weatherData.add(new Weather(2, 2, "London", "Rainy", 15.0, 75.0, 18.3, "RAINY"));
        weatherData.add(new Weather(3, 3, "Paris", "Sunny", 24.0, 55.0, 8.2, "SUNNY"));
        weatherData.add(new Weather(4, 4, "Tokyo", "Clear", 18.5, 60.0, 5.0, "CLEAR"));
        weatherData.add(new Weather(5, 5, "Sydney", "Warm and Sunny", 28.0, 50.0, 10.5, "SUNNY"));
    }

    /**
     * Get weather for a specific city by city ID
     */
    public Optional<Weather> getWeatherByCityId(Integer cityId) {
        return weatherData.stream()
                .filter(weather -> weather.getCityId().equals(cityId))
                .findFirst();
    }

    /**
     * Get weather for a specific city by city name
     */
    public Optional<Weather> getWeatherByCityName(String cityName) {
        return weatherData.stream()
                .filter(weather -> weather.getCityName().equalsIgnoreCase(cityName))
                .findFirst();
    }

    /**
     * Get all weather data
     */
    public List<Weather> getAllWeather() {
        return new ArrayList<>(weatherData);
    }

    /**
     * Update weather for a city
     */
    public Optional<Weather> updateWeather(Integer cityId, Weather updatedWeather) {
        return weatherData.stream()
                .filter(weather -> weather.getCityId().equals(cityId))
                .findFirst()
                .map(weather -> {
                    weather.setTemperature(updatedWeather.getTemperature());
                    weather.setHumidity(updatedWeather.getHumidity());
                    weather.setWindSpeed(updatedWeather.getWindSpeed());
                    weather.setCondition(updatedWeather.getCondition());
                    weather.setDescription(updatedWeather.getDescription());
                    return weather;
                });
    }

    /**
     * Add weather for a new city
     */
    public Weather addWeather(Weather weather) {
        int nextId = weatherData.stream()
                .map(Weather::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
        weather.setId(nextId);
        weatherData.add(weather);
        return weather;
    }
}
