# Docker Deployment Guide

## Prerequisites
- Docker installed on your system
- Docker Compose (optional, for easier deployment)

## Option 1: Using Docker Compose (Recommended)

### Build and Run
```bash
cd d:\automation-projects\sample-application-code-review
docker-compose up --build
```

### Access the Application
```
http://localhost:8080
```

### Stop the Application
```bash
docker-compose down
```

---

## Option 2: Using Docker Commands

### Step 1: Build the Docker Image
```bash
cd d:\automation-projects\sample-application-code-review
docker build -t weather-service:1.0 .
```

### Step 2: Run the Container
```bash
docker run -d \
  --name weather-service \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  weather-service:1.0
```

### Step 3: Verify the Container is Running
```bash
docker ps
```

### Access the Application
```
http://localhost:8080
```

---

## Useful Docker Commands

### View Container Logs
```bash
docker logs weather-service
docker logs -f weather-service  # Follow logs in real-time
```

### Check Container Health
```bash
docker inspect --format='{{.State.Health.Status}}' weather-service
```

### Stop the Container
```bash
docker stop weather-service
```

### Start the Container
```bash
docker start weather-service
```

### Remove the Container
```bash
docker rm weather-service
```

### Remove the Image
```bash
docker rmi weather-service:1.0
```

---

## API Testing

### Get All Cities
```bash
curl http://localhost:8080/api/cities
```

### Get Weather by City ID
```bash
curl http://localhost:8080/api/weather/city/1
```

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

---

## Multi-Stage Build Explanation

The Dockerfile uses a multi-stage build:

1. **Builder Stage**: Uses `maven:3.8.4-openjdk-11` to build the JAR
2. **Runtime Stage**: Uses lightweight `openjdk:11-jre-slim` to run the application

This approach:
- Reduces final image size (only JRE, not JDK)
- Improves security (no build tools in production)
- Faster deployment

---

## Environment Variables

You can customize the application using environment variables:

```bash
docker run -d \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  -e SERVER_PORT=8080 \
  weather-service:1.0
```

---

## Pushing to Docker Registry (Optional)

### Docker Hub
```bash
docker tag weather-service:1.0 your-username/weather-service:1.0
docker push your-username/weather-service:1.0
```

### Pull and Run
```bash
docker run -d -p 8080:8080 your-username/weather-service:1.0
```
