# Online Learning Platform (Microservices)

A Spring Boot microservice suite for an online learning platform with student, teacher, and admin roles. Identity/RBAC, profiles, and learning flows are split into independently deployable services behind an API gateway.

## Services
- **Discovery Server** (`discovery-server`): Eureka registry for service discovery.
- **API Gateway** (`api-gateway`): Single entry point, JWT-aware routing, request validation.
- **Identity Service** (`authentication-service`, app name `identity-service`): Account registration/login, JWT issuance, role changes; publishes user events to Kafka.
- **Profile Service** (`user-service`, app name `profile-service`): Profile data, cached reads via Redis; consumes identity and learning events to build activity history.
- **Learning Service** (`inventory-service`, app name `learning-service`): Courses, assessments, submissions, enrollments; emits learning events for downstream analytics/profile updates.

## Core Design Choices
- **Security & RBAC**: Stateless JWTs (HS256) with claims for `sub` (accountId), `email`, and `role`. Gateway calls the identity validate endpoint; downstream services parse claims for authorization.
- **Messaging**: Kafka topics `identity.accounts` and `learning.events` used for cross-service communication (profile creation, activity tracking).
- **Caching**: Redis-backed caches for frequently read resources (profiles, course catalog/details).
- **Storage**: Each service owns its Postgres schema (identity/profile/learning) to preserve bounded contexts.
- **Observability**: Actuator + Prometheus registry enabled on all services.

## Domain Overview
- **Students**: enroll in courses, access published content, submit assessments.
- **Teachers**: create/update/publish courses, build assessments, review submissions, grade.
- **Admins**: manage roles and monitor usage via activity logs.

## Running Locally
### Prerequisites
- Docker & Docker Compose
- Java 21 + Maven (for local builds/tests)

### With Docker Compose
```bash
docker compose up --build
```
Services start on:
- Gateway: http://localhost:8080
- Eureka: http://localhost:8761
- Kafka (host listener): localhost:9094
- Redis: localhost:6379
- Postgres: 5433 (identity), 5434 (profile), 5435 (learning)

### Local Maven Build (optional)
```bash
mvn -DskipTests package
```

## API Quickstart
Use the bundled Postman collection `Inv-app.postman_collection-1.json` (base_url defaults to the gateway).
- Register/Login via identity service to obtain `access_token`.
- Create/Publish courses as a teacher, enroll as a student, submit/grade assessments.
- Profile endpoints expose self-service profile management and allow instructor/admin lookups.

## CI/CD
- GitHub Actions workflow `.github/workflows/ci.yml` (added) runs Maven builds and caches dependencies. Extend with image publishing as needed.

## Configuration
Key environment variables (defaults set in `application.properties`):
- `JWT_SECRET` (shared HMAC secret for all services)
- `EUREKA_SERVER` (gateway/services use discovery), `KAFKA_BOOTSTRAP_SERVERS`, `REDIS_HOST/PORT`
- Service-specific DB URLs: `IDENTITY_DB_URL`, `PROFILE_DB_URL`, `LEARNING_DB_URL`

## Next Steps
- Add OpenAPI docs per service.
- Expand analytics/monitoring based on `learning.events` stream.
- Harden auth with refresh tokens and key rotation.
