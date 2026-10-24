# Payment Service

Consumes `reservation.created`, creates an idempotent demo VA record, and exposes payment lookup and a demo confirmation endpoint. Confirmation is published through the payment outbox as `payment.confirmed`, which Reservation Service consumes. Replace the demo callback with a signed provider adapter for a real payment integration.
