package com.zerooversell.reservation.messaging;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.UUID;

public record ReservationCreatedEvent(UUID eventId, UUID reservationId, UUID saleId,
                                      UUID customerId, BigDecimal amount, String currency, Instant expiresAt) {}
