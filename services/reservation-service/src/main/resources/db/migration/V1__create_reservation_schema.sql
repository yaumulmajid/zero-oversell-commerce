CREATE TABLE flash_stock (
    sale_id UUID PRIMARY KEY,
    qty INTEGER NOT NULL CHECK (qty >= 0)
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

-- Demo campaign; replace with campaign-service provisioning in a multi-service deployment.
INSERT INTO flash_stock (sale_id, qty)
VALUES ('00000000-0000-0000-0000-000000000100', 100);
