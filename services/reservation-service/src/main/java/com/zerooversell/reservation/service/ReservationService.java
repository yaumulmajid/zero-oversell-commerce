package com.zerooversell.reservation.service;

import com.zerooversell.reservation.entity.Reservation;
import com.zerooversell.reservation.entity.OutboxEvent;
import com.zerooversell.reservation.entity.ReservationStatus;
import com.zerooversell.reservation.messaging.ReservationCreatedEvent;
import com.zerooversell.reservation.messaging.ReservationExpiredEvent;
import com.zerooversell.reservation.messaging.ReservationPaymentDecisionEvent;
import com.zerooversell.reservation.repository.FlashStockRepository;
import com.zerooversell.reservation.repository.OutboxEventRepository;
import com.zerooversell.reservation.repository.ReservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class ReservationService {
    private final FlashStockRepository stockRepository;
    private final ReservationRepository reservationRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final int ttlMinutes;

    public ReservationService(FlashStockRepository stockRepository,
                              ReservationRepository reservationRepository,
                              OutboxEventRepository outboxRepository,
                              ObjectMapper objectMapper,
                              @Value("${stockguard.reservation-ttl-minutes:20}") int ttlMinutes) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.ttlMinutes = ttlMinutes;
    }

    @Transactional
    public Reservation reserve(UUID saleId, UUID customerId, String idempotencyKey) {
        var existing = reservationRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) return existing.get();
        if (reservationRepository.existsBySaleIdAndCustomerId(saleId, customerId)) {
            throw new DuplicateCustomerReservationException();
        }
        if (stockRepository.reserveOne(saleId) != 1) throw new OutOfStockException();
        var reservation = reservationRepository.save(new Reservation(UUID.randomUUID(), saleId, customerId,
                idempotencyKey, Instant.now().plusSeconds(ttlMinutes * 60L)));
        var stock = stockRepository.findById(saleId).orElseThrow(OutOfStockException::new);
        var event = new ReservationCreatedEvent(UUID.randomUUID(), reservation.getId(), saleId,
                customerId, stock.getUnitPrice(), stock.getCurrency(), reservation.getExpiresAt());
        try {
            outboxRepository.save(new OutboxEvent("reservation.created", reservation.getId().toString(),
                    objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize reservation event", exception);
        }
        return reservation;
    }

    @Transactional(readOnly = true)
    public Reservation get(UUID reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
    }

    @Transactional
    public int provisionInventory(UUID saleId, int quantity, java.math.BigDecimal unitPrice, String currency) {
        if (quantity < 0) throw new IllegalArgumentException("quantity must be non-negative");
        return stockRepository.provision(saleId, quantity, unitPrice, currency.toUpperCase());
    }

    @Transactional
    public void confirmPayment(UUID reservationId) {
        var reservation = reservationRepository.findByIdForUpdate(reservationId).orElse(null);
        if (reservation == null) return;
        boolean accepted = reservation.getStatus() == ReservationStatus.PAID;
        if (reservation.getStatus() == ReservationStatus.PENDING_PAYMENT) {
            if (reservation.getExpiresAt().isAfter(Instant.now())) {
                accepted = reservation.markPaid();
            } else if (reservation.expire()) {
                stockRepository.releaseOne(reservation.getSaleId());
                saveReservationEvent("reservation.expired", reservation.getId(),
                        new ReservationExpiredEvent(UUID.randomUUID(), reservation.getId()));
            }
        }
        var decision = new ReservationPaymentDecisionEvent(UUID.randomUUID(), reservationId, accepted);
        saveReservationEvent(accepted ? "reservation.payment-accepted" : "reservation.payment-rejected",
                reservationId, decision);
    }

    @Transactional
    public int expireDueReservations() {
        var due = reservationRepository.findTop100ByStatusAndExpiresAtBeforeOrderByExpiresAtAsc(
                ReservationStatus.PENDING_PAYMENT, Instant.now());
        int expired = 0;
        for (var reservation : due) {
            if (reservation.expire()) {
                stockRepository.releaseOne(reservation.getSaleId());
                saveReservationEvent("reservation.expired", reservation.getId(),
                        new ReservationExpiredEvent(UUID.randomUUID(), reservation.getId()));
                expired++;
            }
        }
        return expired;
    }

    private void saveReservationEvent(String topic, UUID reservationId, Object event) {
        try {
            outboxRepository.save(new OutboxEvent(topic, reservationId.toString(),
                    objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize reservation event", exception);
        }
    }
}
