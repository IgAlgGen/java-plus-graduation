# syntax=docker/dockerfile:1.6
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY core/pom.xml core/pom.xml
COPY core/internal-api/pom.xml core/internal-api/pom.xml
COPY core/user-service/pom.xml core/user-service/pom.xml
COPY core/event-service/pom.xml core/event-service/pom.xml
COPY core/request-service/pom.xml core/request-service/pom.xml
COPY core/category-service/pom.xml core/category-service/pom.xml
COPY core/compilation-service/pom.xml core/compilation-service/pom.xml
COPY core/comment-service/pom.xml core/comment-service/pom.xml
COPY infra/pom.xml infra/pom.xml
COPY infra/config-server/pom.xml infra/config-server/pom.xml
COPY infra/discovery-server/pom.xml infra/discovery-server/pom.xml
COPY infra/gateway-server/pom.xml infra/gateway-server/pom.xml
COPY stats-service/pom.xml stats-service/pom.xml
COPY stats-service/stats-proto/pom.xml stats-service/stats-proto/pom.xml
COPY stats-service/stats-avro/pom.xml stats-service/stats-avro/pom.xml
COPY stats-service/aggregator/pom.xml stats-service/aggregator/pom.xml
COPY stats-service/collector/pom.xml stats-service/collector/pom.xml
COPY stats-service/analyzer/pom.xml stats-service/analyzer/pom.xml
COPY stats-service/stats-client/pom.xml stats-service/stats-client/pom.xml
COPY stats-service/stats-dto/pom.xml stats-service/stats-dto/pom.xml
COPY stats-service/stats-server/pom.xml stats-service/stats-server/pom.xml

RUN --mount=type=cache,id=maven-repo,target=/root/.m2 mvn -B dependency:go-offline -DskipTests -pl infra/config-server -am

COPY . .

RUN --mount=type=cache,id=maven-repo,target=/root/.m2 mvn -B clean package -DskipTests -pl infra/config-server -am

FROM eclipse-temurin:21

WORKDIR /app

COPY --from=build /app/infra/config-server/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
