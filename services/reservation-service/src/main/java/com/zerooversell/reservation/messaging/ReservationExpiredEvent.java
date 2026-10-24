package com.zerooversell.reservation.messaging;
import java.util.UUID;
public record ReservationExpiredEvent(UUID eventId, UUID reservationId) {}
