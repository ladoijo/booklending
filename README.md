# Book Lending API

A Spring Boot backend service for managing a simple book lending workflow (books, members, loans,
returns).  
Built with modern Java + Gradle, and designed to be easy to run locally with PostgreSQL + Liquibase.

---

## Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [Database & Migrations](#database--migrations)
- [API Documentation (Swagger/OpenAPI)](#api-documentation-swaggeropenapi)
- [Actuator](#actuator)
- [Build & Test](#build--test)
- [Docker](#docker)
- [Troubleshooting](#troubleshooting)

---

## Features

- CRUD for domain resources (example: `books`, `members`, `loans`)
- Input validation + consistent error responses
- Database migrations with Liquibase
- OpenAPI/Swagger UI for API exploration
- Spring Security support (public endpoints + secured endpoints)
- Actuator endpoints for health & metrics

---

## Tech Stack

- **Java:** 21
- **Framework:** Spring Boot (4.x)
- **Build Tool:** Gradle (Kotlin DSL)
- **Database:** PostgreSQL
- **Migrations:** Liquibase
- **Docs:** springdoc-openapi (Swagger UI)
- **Observability:** Spring Boot Actuator

---

## Project Structure

Typical layout:

```
booklending/
  src/main/java/...        # application code
  src/main/resources/      # application.yml/properties, Liquibase changelogs, etc.
  src/test/java/...        # tests
  build.gradle.kts
  settings.gradle.kts
```

---

## Prerequisites

- JDK **21**
- Gradle (or use the Gradle Wrapper: `./gradlew`)
- PostgreSQL running locally (or via Docker)

---

## Quick Start

### 1) Clone & enter the project

```bash
git clone https://github.com/ladoijo/booklending
cd booklending
```

### 2) Configure environment

Edit `src/main/resources/application.properties` (or create `application-local.properties`).

### 3) Configure database

Edit `src/main/resources/application.properties` (or create `application-local.properties`).

### 4) Run

```bash
./gradlew bootRun
```

App will be available at:

- API base: `http://localhost:8080`
- Health: `http://localhost:8080/actuator/health`

---

## Configuration

Suggested configuration keys (adapt to your project):

### Database

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library?currentSchema=booklending
spring.datasource.username=libraryuser
spring.datasource.password=librarypass
```

### Logging (optional)

```properties
logging.level.root=INFO
logging.level.org.springframework.web=INFO
```

### Security (optional)

If you secure most endpoints, keep docs/health public:

```java
// Example patterns to permit without auth:
// /v3/api-docs/**, /swagger-ui/**, /swagger-ui.html, /actuator/health/**
```

---

## Database & Migrations

This project uses **Liquibase** to manage schema changes.

Common workflows:

### Run migrations automatically on startup

Enabled by default when:

```properties
spring.liquibase.enabled=true
```

### Where to put changelogs

Usually:

- `src/main/resources/db/changelog/master.yaml` (or `.xml` / `.json`)

> If you ever see: `ERROR: no schema has been selected to create in`, ensure your datasource points
> to the right DB and schema, or explicitly set the default schema in PostgreSQL / Liquibase config.

---

## API Documentation (Swagger/OpenAPI)

After running the app:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

If Swagger UI shows `{"error":"Unauthorized"}`, your security config is likely
protecting `/swagger-ui/**` or `/v3/api-docs/**`. Add them to your allowlist or provide an auth
mechanism for Swagger.

---

## Actuator

Common useful endpoints:

- Health: `GET /actuator/health`
- Info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`

To expose endpoints:

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.probes.enabled=true
```

---

## Build & Test

### Build

```bash
./gradlew clean build
```

### Run tests

```bash
./gradlew test
```

### Create executable jar

```bash
./gradlew bootJar
```

---

## Docker

If your repo includes a `Dockerfile`:

### Build & Run

```bash
docker compose up -d --build
```

### Enable BuildKit (recommended)

```bash
export DOCKER_BUILDKIT=1
docker build -t booklending:local .
```

---

## Troubleshooting

### Swagger UI returns `Unauthorized`

- Ensure these are permitted in Spring Security:
    - `/v3/api-docs/**`
    - `/swagger-ui/**`
    - `/swagger-ui.html`
- If you use JWT, Swagger may need a Bearer token configured in OpenAPI.

### Liquibase: `no schema has been selected to create in`

- Ensure the database exists and your user has permission.
- Consider setting:
    - `spring.liquibase.default-schema=public` (or your schema)
