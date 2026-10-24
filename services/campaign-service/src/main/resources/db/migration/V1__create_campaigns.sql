CREATE TABLE campaigns (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL CHECK (unit_price > 0),
    currency VARCHAR(3) NOT NULL,
    initial_quantity INTEGER NOT NULL CHECK (initial_quantity >= 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'CLOSED')),
    created_at TIMESTAMPTZ NOT NULL
);
