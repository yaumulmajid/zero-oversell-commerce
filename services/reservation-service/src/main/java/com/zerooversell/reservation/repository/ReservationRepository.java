package com.zerooversell.reservation.repository;

import com.zerooversell.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findByIdempotencyKey(String idempotencyKey);
    boolean existsBySaleIdAndCustomerId(UUID saleId, UUID customerId);
}
