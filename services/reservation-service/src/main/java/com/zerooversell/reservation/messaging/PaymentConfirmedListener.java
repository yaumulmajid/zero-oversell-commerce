package com.zerooversell.reservation.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerooversell.reservation.service.ReservationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConfirmedListener {
    private final ObjectMapper objectMapper;
    private final ReservationService reservationService;

    public PaymentConfirmedListener(ObjectMapper objectMapper, ReservationService reservationService) {
        this.objectMapper = objectMapper;
        this.reservationService = reservationService;
    }

    @KafkaListener(topics = "payment.confirmed", groupId = "reservation-service")
    public void onPaymentConfirmed(String payload) throws Exception {
        var event = objectMapper.readValue(payload, PaymentConfirmedEvent.class);
        reservationService.confirmPayment(event.reservationId());
    }
}
