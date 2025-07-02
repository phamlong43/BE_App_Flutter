# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/app-0.0.1-SNAPSHOT.jar"]

