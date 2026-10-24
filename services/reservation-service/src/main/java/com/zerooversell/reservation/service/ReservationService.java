package com.zerooversell.reservation.service;

import com.zerooversell.reservation.entity.Reservation;
import com.zerooversell.reservation.repository.FlashStockRepository;
import com.zerooversell.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class ReservationService {
    private final FlashStockRepository stockRepository;
    private final ReservationRepository reservationRepository;
    private final int ttlMinutes;

    public ReservationService(FlashStockRepository stockRepository,
                              ReservationRepository reservationRepository,
                              @Value("${stockguard.reservation-ttl-minutes:20}") int ttlMinutes) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
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
        return reservationRepository.save(new Reservation(UUID.randomUUID(), saleId, customerId,
                idempotencyKey, Instant.now().plusSeconds(ttlMinutes * 60L)));
    }
}
