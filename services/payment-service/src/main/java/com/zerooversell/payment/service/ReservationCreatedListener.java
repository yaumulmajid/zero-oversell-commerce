package com.zerooversell.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationCreatedListener {
    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;
    public ReservationCreatedListener(ObjectMapper objectMapper, PaymentService paymentService) {
        this.objectMapper = objectMapper; this.paymentService = paymentService;
    }
    @KafkaListener(topics = "reservation.created", groupId = "payment-service")
    public void onReservationCreated(String payload) throws Exception {
        paymentService.createFrom(objectMapper.readValue(payload, ReservationCreatedEvent.class));
    }

    @KafkaListener(topics = "reservation.payment-accepted", groupId = "payment-service")
    public void onPaymentAccepted(String payload) throws Exception {
        var event = objectMapper.readValue(payload, ReservationPaymentDecisionEvent.class);
        paymentService.reservationAccepted(event.reservationId());
    }

    @KafkaListener(topics = "reservation.payment-rejected", groupId = "payment-service")
    public void onPaymentRejected(String payload) throws Exception {
        var event = objectMapper.readValue(payload, ReservationPaymentDecisionEvent.class);
        paymentService.reservationRejected(event.reservationId());
    }

    @KafkaListener(topics = "reservation.expired", groupId = "payment-service")
    public void onReservationExpired(String payload) throws Exception {
        var event = objectMapper.readValue(payload, ReservationExpiredEvent.class);
        paymentService.reservationExpired(event.reservationId());
    }

    public record ReservationPaymentDecisionEvent(java.util.UUID eventId, java.util.UUID reservationId, boolean accepted) {}
    public record ReservationExpiredEvent(java.util.UUID eventId, java.util.UUID reservationId) {}
}
