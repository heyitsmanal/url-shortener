# URL Shortener (Java 17 + Spring Boot 3)

A production-style URL shortener with click analytics.

## Features
- Create short links (`POST /api/v1/links`) with `X-API-Key` header
- Redirect (`GET /r/{code}`) recording timestamp, IP hash (peppered), user-agent, referrer
- Stats summary (`GET /api/v1/links/{code}/stats`)
- Pagination & search (`GET /api/v1/links?query=&page=&size=`)
- Swagger UI at `/swagger-ui/index.html`

## Run (Dev, H2)
```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```
Dev profile is active by default (H2 in-memory).

## Run with Docker + Postgres
```bash
docker compose up --build
```
App: http://localhost:8080

## API Quick Test
```bash
# Create
curl -s -X POST http://localhost:8080/api/v1/links   -H "Content-Type: application/json"   -H "X-API-Key: dev-key"   -d '{"targetUrl":"https://example.org","customCode":"hello","createdBy":"manal"}'

# Redirect
curl -i http://localhost:8080/r/hello

# Stats
curl -s http://localhost:8080/api/v1/links/hello/stats
```

## Build & Test
```bash
mvn -q -DskipTests package
mvn test
```

## CI
A minimal GitHub Actions workflow runs `mvn -B -ntp -q -DskipTests verify` on pushes & PRs.
