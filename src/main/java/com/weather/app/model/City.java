package com.weather.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private Integer id;
    private String name;
    private String country;
    private Double latitude;
    private Double longitude;
}
