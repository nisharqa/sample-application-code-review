package com.weather.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Issue: Missing validation annotations
// Should use Jakarta Bean Validation (javax.validation.constraints.*)

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    // Issue: No validation on ID
    // Should use: @NotNull @Positive  
    private Integer id;
    
    // Issue: No validation - cityId can be null or invalid
    // Should use: @NotNull @Positive
    private Integer cityId;
    
    // Issue: No validation - cityName can be null or empty
    // Should use: @NotBlank
    private String cityName;
    
    // Issue: No validation - description has no constraints
    // Should use: @Size(max = 500)
    private String description;
    
    // Issue: No temperature range validation
    // Should use: @Min(-100) @Max(60) for realistic temperature range
    private Double temperature;
    
    // Issue: No humidity range validation
    // Should use: @Min(0) @Max(100) for percentage
    private Double humidity;
    
    // Issue: No wind speed validation
    // Should use: @Min(0) for non-negative values
    private Double windSpeed;
    
    // Issue: No validation on condition - should be enum or restricted set
    // Should use: @Pattern or enum type
    private String condition;
}
