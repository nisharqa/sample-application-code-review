package com.weather.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private Integer id;
    private Integer cityId;
    private String cityName;
    private String description;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private String condition;
}
