# ADR-001: Event-driven architecture with Kafka

- Status: Accepted
- Date: 2026-10-15

## Context
Logistics workflows (booking, routing, carrier assignment, tracking, billing) are long-running,
involve external parties that fail or respond late, and produce high-volume telemetry.
Services must evolve and scale independently.

## Decision
Services communicate asynchronously through Kafka topics. Each service owns its database.
Events are published via the transactional outbox pattern; consumers are idempotent.
Events are keyed by shipment id to preserve per-shipment ordering.

## Alternatives considered
- Synchronous REST between services: simple, but couples availability and creates cascading failures.
- RabbitMQ / SQS: good for task queues, but weaker for replay, ordering by key, and stream processing.
- Distributed transactions (2PC): rejected; poor availability and fit with microservices. Use sagas.

## Consequences
- (+) Loose coupling, independent scaling, replayable history, natural audit trail
- (-) Eventual consistency, more operational surface, need for schema governance and idempotency
- Delivery is at-least-once; duplicates are handled by consumers
