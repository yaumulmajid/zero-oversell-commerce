package com.zerooversell.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "flash_stock")
public class FlashStock {
    @Id
    private UUID saleId;
    @Column(nullable = false)
    private int qty;

    protected FlashStock() {}

    public UUID getSaleId() { return saleId; }
    public int getQty() { return qty; }
}
