FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY */pom.xml ./
COPY */*/pom.xml ./
COPY . .

RUN mvn -B clean package -DskipTests -pl core/event-service -am

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/core/event-service/target/event-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
