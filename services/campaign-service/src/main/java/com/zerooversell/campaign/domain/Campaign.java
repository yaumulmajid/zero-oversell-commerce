package com.zerooversell.campaign.domain;

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
@Table(name = "campaigns")
public class Campaign {
    @Id private UUID id;
    @Column(nullable = false) private String name;
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2) private BigDecimal unitPrice;
    @Column(nullable = false, length = 3) private String currency;
    @Column(name = "initial_quantity", nullable = false) private int initialQuantity;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private CampaignStatus status;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected Campaign() {}

    public Campaign(UUID id, String name, BigDecimal unitPrice, String currency, int initialQuantity) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.currency = currency;
        this.initialQuantity = initialQuantity;
        this.status = CampaignStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public String getCurrency() { return currency; }
    public int getInitialQuantity() { return initialQuantity; }
    public CampaignStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
