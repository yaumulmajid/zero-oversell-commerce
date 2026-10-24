package com.zerooversell.payment.service;

import java.util.UUID;
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(UUID reservationId) { super("Payment not found for reservation " + reservationId); }
}
