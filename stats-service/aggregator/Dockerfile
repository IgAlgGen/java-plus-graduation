FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY */pom.xml ./
COPY */*/pom.xml ./
COPY . .

RUN mvn -B clean package -DskipTests -pl stats-service/aggregator -am

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/stats-service/aggregator/target/*jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
