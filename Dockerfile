#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY build/libs/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Build stage
FROM gradle:8.14.0-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]