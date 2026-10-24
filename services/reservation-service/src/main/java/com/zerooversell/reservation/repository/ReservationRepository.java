package com.zerooversell.reservation.repository;

import com.zerooversell.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.zerooversell.reservation.entity.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findByIdempotencyKey(String idempotencyKey);
    boolean existsBySaleIdAndCustomerId(UUID saleId, UUID customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Reservation> findTop100ByStatusAndExpiresAtBeforeOrderByExpiresAtAsc(
            ReservationStatus status, Instant now);

    @Query("select r from Reservation r where r.id = :reservationId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Reservation> findByIdForUpdate(@Param("reservationId") UUID reservationId);
}
