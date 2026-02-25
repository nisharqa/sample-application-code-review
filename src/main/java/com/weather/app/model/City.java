package com.weather.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Issue: Missing validation annotations
// Should use: @NotNull, @NotBlank, @Size, @Min, @Max, etc.
// Should use: Jakarta Bean Validation (javax.validation.constraints.*)

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    // Issue: No validation - ID can be null or negative
    // Should use: @NotNull @Positive
    private Integer id;
    
    // Issue: No validation - name can be null, empty, or contain invalid characters
    // Should use: @NotBlank(message = "Name is required")
    // Should use: @Size(min = 2, max = 100)
    private String name;
    
    // Issue: No validation - country can be null or empty
    // Should use: @NotBlank(message = "Country is required")
    private String country;
    
    // Issue: No range validation for coordinates
    // Should use: @Min(-90) @Max(90) for latitude
    private Double latitude;
    
    // Issue: No range validation for coordinates  
    // Should use: @Min(-180) @Max(180) for longitude
    private Double longitude;
}
