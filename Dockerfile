# Use the official Gradle image as the base image
FROM gradle:7.2.0-jdk11 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files to the container
COPY build.gradle .
COPY settings.gradle .

# Copy the source code to the container
COPY src/ src/

# Build the application with Gradleser
RUN gradle clean build --no-daemon -x test

# Create a new Docker image with the Java runtime only
FROM adoptopenjdk:11-jre-hotspot

ARG offers_path
ARG player_dataset_path
ARG events_path
ARG APP_PORT

ENV APP_PORT=$APP_PORT

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the builder stage to the container
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

COPY $offers_path /app$offers_path
COPY $player_dataset_path /app$player_dataset_path
COPY $events_path /app$events_path

# Expose the port on which the Spring Boot application listens
EXPOSE $APP_PORT

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
