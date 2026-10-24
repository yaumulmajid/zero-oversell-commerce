package com.zerooversell.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "flash_stock")
public class FlashStock {
    @Id
    private UUID saleId;
    @Column(nullable = false)
    private int qty;
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false, length = 3)
    private String currency;

    protected FlashStock() {}

    public UUID getSaleId() { return saleId; }
    public int getQty() { return qty; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public String getCurrency() { return currency; }
}
