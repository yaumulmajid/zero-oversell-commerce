# Campaign Service

Owns campaign metadata, price, and initial quantity. `POST /api/v1/campaigns` creates a campaign and provisions the corresponding stock row and locked price through Reservation Service. See [high level design](../../docs/architecture/high-level-design.md).
