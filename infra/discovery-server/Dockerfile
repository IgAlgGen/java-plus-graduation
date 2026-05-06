# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY */pom.xml ./
COPY */*/pom.xml ./

COPY . .

RUN mvn -B clean package -DskipTests -pl infra/discovery-server -am

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# infra/discovery-server/Dockerfile
COPY --from=builder /build/infra/discovery-server/target/discovery-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
