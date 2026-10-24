# Expiry Worker

Polls Reservation Service every five seconds for due reservations. Reservation Service owns the database transaction, conditionally transitions `PENDING_PAYMENT` to `EXPIRED`, then releases stock once. The worker never accesses the reservation database directly.
