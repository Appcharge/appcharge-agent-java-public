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

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the builder stage to the container
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

COPY /src/main/resources/offers.json /app/src/main/resources/offers.json
COPY /src/main/resources/player-dataset.json /app/src/main/resources/player-dataset.json

# Expose the port on which the Spring Boot application listens
EXPOSE 8080

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
