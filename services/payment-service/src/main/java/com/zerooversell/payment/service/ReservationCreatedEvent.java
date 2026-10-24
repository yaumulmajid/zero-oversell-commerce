package com.zerooversell.payment.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReservationCreatedEvent(UUID eventId, UUID reservationId, UUID saleId,
                                      UUID customerId, BigDecimal amount, String currency, Instant expiresAt) {}
