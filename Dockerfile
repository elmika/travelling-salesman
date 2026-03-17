# Stage 1: Build React frontend
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build
# output lands at /app/src/main/resources/static (via vite.config.ts outDir)

# Stage 2: Download Maven dependencies (shared cached layer)
FROM maven:3.6.3-jdk-11 AS maven-deps
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Stage 3a: Compile and test (no frontend needed for tests)
FROM maven-deps AS test
COPY src ./src
RUN mvn test

# Stage 3b: Package with embedded frontend
FROM maven-deps AS java-build
COPY src ./src
COPY --from=frontend-build /app/src/main/resources/static ./src/main/resources/static
RUN mvn clean package -DskipTests

# Stage 4: Slim runtime image
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=java-build /app/target/tsp-solver-0.1-SETUP.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
