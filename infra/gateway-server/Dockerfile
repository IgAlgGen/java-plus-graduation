# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY */pom.xml ./
COPY */*/pom.xml ./

COPY . .

RUN mvn -B clean package -DskipTests -pl infra/gateway-server -am

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/infra/gateway-server/target/gateway-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
