package com.zerooversell.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    private UUID id;
    @Column(name = "sale_id", nullable = false)
    private UUID saleId;
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;
    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Reservation() {}

    public Reservation(UUID id, UUID saleId, UUID customerId, String idempotencyKey, Instant expiresAt) {
        this.id = id;
        this.saleId = saleId;
        this.customerId = customerId;
        this.idempotencyKey = idempotencyKey;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
        this.status = ReservationStatus.PENDING_PAYMENT;
    }

    public UUID getId() { return id; }
    public UUID getSaleId() { return saleId; }
    public UUID getCustomerId() { return customerId; }
    public ReservationStatus getStatus() { return status; }
    public Instant getExpiresAt() { return expiresAt; }

    public boolean markPaid() {
        if (status != ReservationStatus.PENDING_PAYMENT) return false;
        status = ReservationStatus.PAID;
        return true;
    }

    public boolean expire() {
        if (status != ReservationStatus.PENDING_PAYMENT) return false;
        status = ReservationStatus.EXPIRED;
        return true;
    }
}
