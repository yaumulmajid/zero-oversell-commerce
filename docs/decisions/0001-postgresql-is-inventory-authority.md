# ADR 0001 PostgreSQL is the inventory authority

- **Status:** Accepted
- **Date:** 2026-10-24

## Context

Concurrent requests must not oversell. A cache-only stock counter can drift during failover or message loss, while independent read-then-write operations can race.

## Decision

PostgreSQL is the authoritative store for campaign inventory and reservations. Reservation uses a conditional decrement in the same transaction as the reservation row. Redis is an optional admission and load-shedding layer. Kafka carries asynchronous events through a transactional outbox.

## Consequences

- Inventory correctness depends on the database transaction and constraints.
- The database can become a hot key for a very popular campaign; admission control and load testing are required.
- Redis may reject early but cannot grant the final unit.
- Async workflows are eventually consistent and require idempotent consumers and reconciliation.
