# Use the official Java 19 base image
FROM openjdk:19-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the Spring Boot JAR file from your local machine to the container
COPY target/eClinic001-0.0.1-SNAPSHOT.jar /app/

# Expose the application's port
EXPOSE 1992

# Define the command to run your application
CMD ["java", "-jar", "/app/eClinic001-0.0.1-SNAPSHOT.jar"]

