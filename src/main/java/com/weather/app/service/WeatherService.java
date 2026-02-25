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

    /**
     * N+1 Query Problem: Fetches all cities then queries weather separately for each
     * This simulates fetching parent entities and then querying child entities in a loop
     */
    public List<Weather> getAllWeatherWithCityDetails(CityService cityService) {
        List<Weather> result = new ArrayList<>();
        // First query: get all cities (N)
        List<com.weather.app.model.City> allCities = cityService.getAllCities();
        // N+1: For each city, perform a separate query
        for (com.weather.app.model.City city : allCities) {
            Optional<Weather> weather = weatherData.stream()
                    .filter(w -> w.getCityId().equals(city.getId()))
                    .findFirst();
            weather.ifPresent(result::add);
        }
        return result;
    }

    /**
     * Issue: Magic numbers - Using hardcoded values without explanation
     */
    public List<Weather> getWeatherAboveThreshold() {
        // Issue: Magic number 25 - what does it represent?
        return weatherData.stream()
                .filter(w -> w.getTemperature() > 25)
                .toList();
    }

    /**
     * Issue: More magic numbers and poor naming
     */
    public boolean check(Weather w) {
        // Issue: What do these numbers mean? 15, 30, 80?
        if (w.getTemperature() < 15 || w.getTemperature() > 30) {
            return false;
        }
        if (w.getHumidity() > 80) {
            return false;
        }
        // Magic number: What is 20?
        if (w.getWindSpeed() > 20) {
            return false;
        }
        return true;
    }

    /**
     * Issue: Code duplication - Similar logic to check() method above
     */
    public boolean validateWeather(Weather weather) {
        // Duplicated logic from check() method
        if (weather.getTemperature() < 15 || weather.getTemperature() > 30) {
            return false;
        }
        if (weather.getHumidity() > 80) {
            return false;
        }
        if (weather.getWindSpeed() > 20) {
            return false;
        }
        return true;
    }

    /**
     * Issue: Poor method naming - what does 'process' mean?
     */
    public void process(Weather w) {
        // Issue: Magic numbers everywhere
        if (w.getTemperature() > 35) {
            w.setCondition("HOT");
        } else if (w.getTemperature() < 10) {
            w.setCondition("COLD");
        }
        
        // More magic numbers
        if (w.getHumidity() > 70) {
            w.setDescription("Humid conditions");
        }
    }

    /**
     * Issue: Duplicate code - same calculation repeated
     */
    public double calculateFeelsLike(Weather weather) {
        // Issue: Complex formula with magic numbers and no explanation
        double temp = weather.getTemperature();
        double humidity = weather.getHumidity();
        
        // What do these numbers mean? 0.555, 10, 58, 1.99?
        double feelsLike = temp + 0.555 * ((humidity / 100) * 10 - 10);
        
        // Magic number: 26
        if (temp > 26) {
            // More magic: 0.348, 0.7, 4.25
            feelsLike = temp + 0.348 * humidity - 0.7 * weather.getWindSpeed() - 4.25;
        }
        
        return feelsLike;
    }

    /**
     * Issue: Duplicated calculation logic
     */
    public double getHeatIndex(Weather weather) {
        // Duplicated logic from calculateFeelsLike
        double temp = weather.getTemperature();
        double humidity = weather.getHumidity();
        
        double heatIndex = temp + 0.555 * ((humidity / 100) * 10 - 10);
        
        if (temp > 26) {
            heatIndex = temp + 0.348 * humidity - 0.7 * weather.getWindSpeed() - 4.25;
        }
        
        return heatIndex;
    }
}
