package com.weather.app.service;

import com.weather.app.model.City;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private List<City> cities = new ArrayList<>();

    public CityService() {
        // Initialize with sample data
        cities.add(new City(1, "New York", "USA", 40.7128, -74.0060));
        cities.add(new City(2, "London", "UK", 51.5074, -0.1278));
        cities.add(new City(3, "Paris", "France", 48.8566, 2.3522));
        cities.add(new City(4, "Tokyo", "Japan", 35.6762, 139.6503));
        cities.add(new City(5, "Sydney", "Australia", -33.8688, 151.2093));
    }

    /**
     * Get all cities
     */
    public List<City> getAllCities() {
        return new ArrayList<>(cities);
    }

    /**
     * Get a city by ID
     */
    public Optional<City> getCityById(Integer id) {
        return cities.stream()
                .filter(city -> city.getId().equals(id))
                .findFirst();
    }

    /**
     * Get a city by name
     */
    public Optional<City> getCityByName(String name) {
        return cities.stream()
                .filter(city -> city.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Add a new city
     */
    public City addCity(City city) {
        int nextId = cities.stream()
                .map(City::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
        city.setId(nextId);
        cities.add(city);
        return city;
    }

    /**
     * SQL Injection vulnerability - VULNERABLE: Native query with string concatenation
     */
    public List<City> searchCitiesUnsafe(String filter) {
        // Simulating vulnerable SQL query construction
        String query = "SELECT * FROM cities WHERE name = '" + filter + "' OR country LIKE '%" + filter + "%'";
        System.out.println("Executing query: " + query);
        return cities.stream()
                .filter(c -> c.getName().contains(filter) || c.getCountry().contains(filter))
                .toList();
    }

    /**
     * Missing validation - accepts any input without checks
     */
    public City addCityWithoutValidation(City city) {
        int nextId = cities.stream()
                .map(City::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
        city.setId(nextId);
        cities.add(city);
        return city;
    }
}
