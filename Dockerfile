# syntax=docker/dockerfile:1.6
# ---- build stage ----
FROM eclipse-temurin:21-jdk as build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -x test --no-daemon

# ---- run stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]