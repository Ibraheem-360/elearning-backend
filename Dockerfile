# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory to the container
COPY target/elearning-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 9000 for the backend service
EXPOSE 9000

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
