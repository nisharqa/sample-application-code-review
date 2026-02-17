package com.weather.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Create and configure the OpenAPI specification for the Weather Service API.
     *
     * The returned OpenAPI instance is populated with API metadata including the
     * title, version, description, contact information, and license.
     *
     * @return an OpenAPI instance containing the API metadata (title, version, description, contact, license)
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weather Service API")
                        .version("1.0.0")
                        .description("REST API for managing cities and weather information")
                        .contact(new Contact()
                                .name("Weather Service Team")
                                .email("info@weather-service.com")
                                .url("https://github.com/nisharqa/sample-application-code-review"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}