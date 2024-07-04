# Stage 1: Build stage
FROM maven:3.9.8-eclipse-temurin-22-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:22-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file from the build stage
COPY --from=build /app/target/kontomatik-1.0-SNAPSHOT.jar /app/app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]