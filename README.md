# Logistics Platform

Event-driven logistics platform (shipment booking, routing, carrier assignment, tracking, billing)
built with Spring Boot, Kafka and PostgreSQL. Phase 1 runs locally; phase 2 deploys to AWS.

## Run locally
```bash
gradle wrapper --gradle-version 8.14   # once, to generate ./gradlew
docker compose -f infra/docker-compose.yml up -d
./gradlew :shipment-service:bootRun    # port 8081
./gradlew :routing-service:bootRun     # port 8082

curl -X POST localhost:8081/shipments -H 'Content-Type: application/json' \
  -d '{"origin":"Mumbai","destination":"Delhi","weightKg":120.5}'
```
Watch the routing-service log for the planned route.

## Docs
- [ADR-001: Event-driven architecture with Kafka](docs/adr/ADR-001-event-driven-architecture-with-kafka.md)
