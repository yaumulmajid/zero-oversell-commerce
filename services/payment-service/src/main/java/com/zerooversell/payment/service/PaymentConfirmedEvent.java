package com.zerooversell.payment.service;

import java.util.UUID;

public record PaymentConfirmedEvent(UUID eventId, UUID reservationId, String providerReference) {}
