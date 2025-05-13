# Build Stage
FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/knight-board-1.0-SNAPSHOT.jar ./knight-board.jar
ENTRYPOINT ["java", "-jar", "knight-board.jar"]