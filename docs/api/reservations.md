# Reservation API

## Create reservation

`POST /api/v1/reservations`

Required header: `Idempotency-Key` (unique per logical checkout request).

Request body:

```json
{
  "saleId": "00000000-0000-0000-0000-000000000100",
  "customerId": "00000000-0000-0000-0000-000000000201"
}
```

Successful response (`201 Created`): reservation identifier, campaign and customer identifiers, `PENDING_PAYMENT` status, and expiry timestamp.

Conflict responses (`409 Conflict`): `OUT_OF_STOCK` or `CUSTOMER_ALREADY_RESERVED`.

The same idempotency key should return the original reservation for a retried logical request. The first implementation handles sequential retries; concurrent duplicate-key behavior is a hardening item before production.
