package com.zerooversell.reservation.repository;

import com.zerooversell.reservation.entity.FlashStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface FlashStockRepository extends JpaRepository<FlashStock, UUID> {
    @Modifying
    @Query(value = "UPDATE flash_stock SET qty = qty - 1 WHERE sale_id = :saleId AND qty > 0", nativeQuery = true)
    int reserveOne(@Param("saleId") UUID saleId);
}
