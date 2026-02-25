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

    /**
     * Issue: Returns null instead of empty list - causes NPE in caller
     */
    public List<City> getCitiesByCountry(String country) {
        List<City> result = cities.stream()
                .filter(c -> c.getCountry().equalsIgnoreCase(country))
                .toList();
        // Issue: Returns null when no results instead of empty list
        return result.isEmpty() ? null : result;
    }

    /**
     * Issue: Thread safety - ArrayList not thread-safe, concurrent modifications possible
     * If multiple threads call this simultaneously, race condition can occur
     */
    public void addCityConcurrent(City city) {
        int nextId = cities.stream()
                .map(City::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
        city.setId(nextId);
        // Issue: No synchronization - race condition
        cities.add(city);
    }

    /**
     * Issue: Thread safety - reading and writing without synchronization
     */
    public int getCityCount() {
        // Issue: No synchronization, could get inconsistent count
        return cities.size();
    }

    /**
     * Issue: Inefficient algorithm - O(n*m) complexity
     */
    public List<City> findCommonCities(List<String> cityNames) {
        List<City> result = new ArrayList<>();
        // Issue: Nested loop - inefficient for large datasets
        for (String name : cityNames) {
            for (City city : cities) {
                if (city.getName().equalsIgnoreCase(name)) {
                    result.add(city);
                }
            }
        }
        return result;
    }

    /**
     * Issue: Inefficient sorting - sorting on every call instead of maintaining sorted order
     */
    public List<City> getCitiesSortedByName() {
        // Issue: Sorts entire list every time, could cache sorted list
        return cities.stream()
                .sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
                .toList();
    }

    /**
     * Issue: Memory leak potential - keeping references in static collection
     */
    private static List<City> cachedCities = new ArrayList<>();
    
    public void cacheCity(City city) {
        // Issue: Static collection grows without bounds - memory leak
        cachedCities.add(city);
    }
}
