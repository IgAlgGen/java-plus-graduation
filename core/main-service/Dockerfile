# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy only pom files first to leverage Docker cache
COPY pom.xml .
COPY */pom.xml ./
COPY */*/pom.xml ./

# Copy full source
COPY . .

# Build only this service and the modules it depends on
RUN mvn -B clean package -DskipTests -pl core/main-service -am

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# core/main-service/Dockerfile
COPY --from=builder /build/core/main-service/target/main-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
