# Multi-stage build for Spring Boot application with Gradle
# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy Gradle files first to leverage Docker layer caching
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon

# Copy source code and build
COPY src ./src
RUN gradle bootJar --no-daemon -x test

# Runtime stage
FROM openjdk:17-jdk-slim

# Create non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to spring user
RUN chown spring:spring app.jar
RUN mkdir -p /app/logs/SYS && chown -R spring:spring /app/logs

# Switch to non-root user
USER spring

# Expose port (adjust if your app uses different port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]