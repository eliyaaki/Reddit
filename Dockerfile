# Base image with OpenJDK 17 and Maven
FROM maven:3.8.4-openjdk-17-slim AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file to the container
COPY pom.xml .

# Download project dependencies
RUN mvn dependency:go-offline

# Copy the application source code to the container
COPY src/ ./src/

# Build the application JAR file
RUN mvn package

# Create a new stage with only the necessary files
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file from the previous stage
COPY --from=builder /app/target/reddit-0.0.1-SNAPSHOT.jar ./reddit.jar

# Expose the port on which your application runs
EXPOSE 8080

# Set the entry point command to run your application
ENTRYPOINT ["java", "-jar", "reddit.jar"]
