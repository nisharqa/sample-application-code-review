# OpenAPI Specification

This application uses **Springdoc OpenAPI** for automatic API documentation. Once the application is running, you can access the interactive Swagger UI and API specification at the following endpoints:

## Endpoints

### Swagger UI (Interactive)
- **URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Description**: Interactive web interface to explore, test, and document the APIs

### OpenAPI JSON Specification
- **URL**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)
- **Description**: Raw OpenAPI 3.0 specification in JSON format

### OpenAPI YAML Specification
- **URL**: [http://localhost:8080/api-docs.yaml](http://localhost:8080/api-docs.yaml)
- **Description**: OpenAPI specification in YAML format

## Features

The OpenAPI specification includes:

✅ **Complete API Documentation**
- All endpoints with request/response schemas
- Parameter descriptions and types
- HTTP status codes and responses

✅ **Interactive API Testing**
- Try-it-out functionality in Swagger UI
- Send requests directly from the browser
- View live responses

✅ **API Categorization**
- Cities API endpoints grouped together
- Weather API endpoints grouped together
- Organized by tags for easy navigation

✅ **Detailed Descriptions**
- Operation summaries and descriptions
- Parameter documentation
- Schema definitions for all models

## API Tags

### Cities
Endpoints for managing city information:
- GET /api/cities - Get all cities
- GET /api/cities/{id} - Get city by ID
- GET /api/cities/search - Search city by name
- POST /api/cities - Create a new city

### Weather
Endpoints for managing weather information:
- GET /api/weather - Get all weather data
- GET /api/weather/city/{cityId} - Get weather by city ID
- GET /api/weather/search - Search weather by city name
- PUT /api/weather/city/{cityId} - Update weather for a city
- POST /api/weather - Create new weather data

## Configuration

OpenAPI configuration is defined in `src/main/java/com/weather/app/config/OpenApiConfig.java`:

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weather Service API")
                        .version("1.0.0")
                        .description("REST API for managing cities and weather information")
                        // ... contact and license info
                );
    }
}
```

## Springdoc OpenAPI Configuration

The following properties are configured in `application.properties`:

```properties
# Springdoc OpenAPI Configuration
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
```

## Annotations Used

All API endpoints are annotated with OpenAPI annotations:

- `@Tag` - Groups endpoints by resource
- `@Operation` - Documents endpoint operation with summary and description
- `@Parameter` - Documents request parameters
- `@ApiResponse` - Documents response codes and schemas
- `@ApiResponses` - Multiple response documentation
- `@Schema` - Defines data model schemas

## Dependencies

The following dependency is used for OpenAPI support:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.7.0</version>
</dependency>
```

## Accessing the Documentation

1. **Start the application**:
   ```bash
   mvn clean spring-boot:run
   ```

2. **Open Swagger UI** in your browser:
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **View raw OpenAPI spec**:
   ```
   http://localhost:8080/api-docs
   ```

The Swagger UI provides:
- Complete API documentation
- Live API testing
- Schema visualization
- Mock server capabilities (in some configurations)

## Integration with Docker

When running the application in Docker, the Swagger UI and API docs are accessible at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/api-docs`

Ensure port 8080 is properly exposed in your Docker configuration.
