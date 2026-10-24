package com.zerooversell.reservation.dto;

import com.zerooversell.reservation.entity.Reservation;
import java.time.Instant;
import java.util.UUID;

public record ReservationResponse(UUID reservationId, UUID saleId, UUID customerId,
                                  String status, Instant expiresAt) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getSaleId(),
                reservation.getCustomerId(), reservation.getStatus().name(), reservation.getExpiresAt());
    }
}
