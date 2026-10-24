package com.zerooversell.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id private UUID id;
    @Column(name = "reservation_id", nullable = false, unique = true) private UUID reservationId;
    @Column(name = "sale_id", nullable = false) private UUID saleId;
    @Column(name = "customer_id", nullable = false) private UUID customerId;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency;
    @Column(name = "provider_reference", nullable = false, unique = true) private String providerReference;
    @Column(name = "expires_at", nullable = false) private Instant expiresAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PaymentStatus status;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected Payment() {}

    public Payment(UUID reservationId, UUID saleId, UUID customerId, BigDecimal amount,
                   String currency, Instant expiresAt) {
        this.id = UUID.randomUUID();
        this.reservationId = reservationId;
        this.saleId = saleId;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.expiresAt = expiresAt;
        this.providerReference = "DEMO-VA-" + UUID.randomUUID();
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public boolean requestConfirmation(Instant now) {
        if (status != PaymentStatus.PENDING || now.isAfter(expiresAt)) return false;
        status = PaymentStatus.CONFIRMATION_PENDING;
        return true;
    }
    public void markPaid() { if (status == PaymentStatus.CONFIRMATION_PENDING) status = PaymentStatus.PAID; }
    public void markExpired() {
        if (status == PaymentStatus.PENDING || status == PaymentStatus.CONFIRMATION_PENDING) status = PaymentStatus.EXPIRED;
    }
    public void reject() { if (status == PaymentStatus.CONFIRMATION_PENDING) status = PaymentStatus.REJECTED; }
    public UUID getReservationId() { return reservationId; }
    public UUID getSaleId() { return saleId; }
    public UUID getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getProviderReference() { return providerReference; }
    public Instant getExpiresAt() { return expiresAt; }
    public PaymentStatus getStatus() { return status; }
}
