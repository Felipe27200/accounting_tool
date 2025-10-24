# 1. First Stage: Set the Java image
# Build the application using maven
FROM openjdk:21-jdk AS builder

WORKDIR /app

COPY . .

# This will execute any commands to
# create a new layer on top of the current image
# The added layer is used in the next step in the Dockerfile
RUN ./mvnw clean package -DskipTest

# 2. Second Stage: Using JRE for run the application
FROM eclipse-temurin:21.0.8_9-jre-jammy AS final

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# These commands will be executed every
# time the container is started

# Sintax [executable, param1, param2]
ENTRYPOINT ["java", "-jar", "app.jar"]