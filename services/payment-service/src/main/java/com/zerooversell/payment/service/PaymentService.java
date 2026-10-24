package com.zerooversell.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerooversell.payment.domain.Payment;
import com.zerooversell.payment.domain.PaymentOutboxEvent;
import com.zerooversell.payment.repository.PaymentOutboxRepository;
import com.zerooversell.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository payments;
    private final PaymentOutboxRepository outbox;
    private final ObjectMapper objectMapper;

    public PaymentService(PaymentRepository payments, PaymentOutboxRepository outbox, ObjectMapper objectMapper) {
        this.payments = payments; this.outbox = outbox; this.objectMapper = objectMapper;
    }

    @Transactional
    public Payment createFrom(ReservationCreatedEvent event) {
        var existing = payments.findByReservationId(event.reservationId());
        if (existing.isPresent()) return existing.get();
        if (!event.expiresAt().isAfter(Instant.now())) return null;
        return payments.save(new Payment(event.reservationId(), event.saleId(), event.customerId(),
                event.amount(), event.currency(), event.expiresAt()));
    }

    @Transactional(readOnly = true)
    public Optional<Payment> findByReservation(UUID reservationId) {
        return payments.findByReservationId(reservationId);
    }

    @Transactional
    public Payment confirm(UUID reservationId) {
        var payment = payments.findByReservationIdForUpdate(reservationId)
                .orElseThrow(() -> new PaymentNotFoundException(reservationId));
        if (payment.getStatus() == com.zerooversell.payment.domain.PaymentStatus.PAID
                || payment.getStatus() == com.zerooversell.payment.domain.PaymentStatus.CONFIRMATION_PENDING) return payment;
        if (!payment.requestConfirmation(Instant.now())) throw new PaymentExpiredException();
        var event = new PaymentConfirmedEvent(UUID.randomUUID(), reservationId, payment.getProviderReference());
        try {
            outbox.save(new PaymentOutboxEvent("payment.confirmed", reservationId.toString(),
                    objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize payment event", exception);
        }
        return payment;
    }

    @Transactional
    public void reservationAccepted(UUID reservationId) {
        payments.findByReservationIdForUpdate(reservationId).ifPresent(Payment::markPaid);
    }

    @Transactional
    public void reservationRejected(UUID reservationId) {
        payments.findByReservationIdForUpdate(reservationId).ifPresent(Payment::reject);
    }

    @Transactional
    public void reservationExpired(UUID reservationId) {
        payments.findByReservationIdForUpdate(reservationId).ifPresent(Payment::markExpired);
    }
}
