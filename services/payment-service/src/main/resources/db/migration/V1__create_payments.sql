CREATE TABLE payments (
    id UUID PRIMARY KEY,
    reservation_id UUID NOT NULL UNIQUE,
    sale_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    amount NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    currency VARCHAR(3) NOT NULL,
    provider_reference VARCHAR(100) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    status VARCHAR(32) NOT NULL CHECK (status IN ('PENDING', 'CONFIRMATION_PENDING', 'PAID', 'EXPIRED', 'REJECTED')),
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE payment_outbox (
    id UUID PRIMARY KEY,
    topic VARCHAR(128) NOT NULL,
    message_key VARCHAR(128) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    published_at TIMESTAMPTZ
);
CREATE INDEX idx_payment_outbox_pending ON payment_outbox (created_at) WHERE published_at IS NULL;
