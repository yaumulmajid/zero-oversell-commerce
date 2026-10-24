# Local API Guide

## Create campaign

`POST http://localhost:8080/api/v1/campaigns`

```json
{
  "name": "Live Flash Sale",
  "unitPrice": 125000,
  "currency": "IDR",
  "quantity": 100
}
```

Campaign Service provisions inventory and the locked price in Reservation Service before returning `201 Created`. The response includes `campaignId`.

## Reserve one unit

`POST http://localhost:8081/api/v1/reservations`

Header: `Idempotency-Key: checkout-001`

```json
{
  "saleId": "<campaignId>",
  "customerId": "00000000-0000-0000-0000-000000000201"
}
```

Returns a pending reservation and `reservationId`. Conflicts return `OUT_OF_STOCK` or `CUSTOMER_ALREADY_RESERVED`.

Get status: `GET http://localhost:8081/api/v1/reservations/{reservationId}`.

## Get and confirm demo payment

Because reservation events are asynchronous, retry payment lookup until the payment consumer has created its record:

`GET http://localhost:8082/api/v1/payments/{reservationId}`

The local-only simulator confirms payment with:

`POST http://localhost:8082/api/v1/payments/{reservationId}/demo-confirm`

Then poll reservation lookup until status becomes `PAID`. This endpoint is not a real provider webhook; it has no signature verification and must not be exposed in production.

## Internal service endpoints

- `POST /internal/v1/inventory` provisions campaign inventory and price. Repeated provisioning for the same ID is ignored.
- `POST /internal/v1/reservations/expire-due` expires up to 100 pending reservations whose TTL has elapsed and returns released stock.

These are open internal endpoints in the local portfolio demo. Add service authentication before deployment outside a trusted local network.
