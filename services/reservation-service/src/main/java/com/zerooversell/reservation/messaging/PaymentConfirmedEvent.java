package com.zerooversell.reservation.messaging;

import java.util.UUID;

public record PaymentConfirmedEvent(UUID eventId, UUID reservationId, String providerReference) {}
