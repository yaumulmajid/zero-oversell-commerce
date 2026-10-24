CREATE TABLE flash_stock (
    sale_id UUID PRIMARY KEY,
    qty INTEGER NOT NULL CHECK (qty >= 0),
    unit_price NUMERIC(12, 2) NOT NULL CHECK (unit_price > 0),
    currency VARCHAR(3) NOT NULL
);

CREATE TABLE reservations (
    id UUID PRIMARY KEY,
    sale_id UUID NOT NULL REFERENCES flash_stock(sale_id),
    customer_id UUID NOT NULL,
    idempotency_key VARCHAR(128) NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL CHECK (status IN ('PENDING_PAYMENT', 'PAID', 'EXPIRED', 'CANCELLED')),
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_reservation_sale_customer UNIQUE (sale_id, customer_id)
);

CREATE INDEX idx_reservations_pending_expiry ON reservations (expires_at)
    WHERE status = 'PENDING_PAYMENT';

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    topic VARCHAR(128) NOT NULL,
    message_key VARCHAR(128) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    published_at TIMESTAMPTZ
);

CREATE INDEX idx_outbox_events_pending ON outbox_events (created_at)
    WHERE published_at IS NULL;
