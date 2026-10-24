package com.zerooversell.reservation.messaging;
import java.util.UUID;
public record ReservationPaymentDecisionEvent(UUID eventId, UUID reservationId, boolean accepted) {}
