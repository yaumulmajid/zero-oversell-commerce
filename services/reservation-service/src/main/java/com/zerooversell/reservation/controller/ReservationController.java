package com.zerooversell.reservation.controller;

import com.zerooversell.reservation.dto.CreateReservationRequest;
import com.zerooversell.reservation.dto.ReservationResponse;
import com.zerooversell.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateReservationRequest request) {
        var reservation = reservationService.reserve(request.saleId(), request.customerId(), idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(reservation));
    }

    @GetMapping("/{reservationId}")
    public ReservationResponse get(@PathVariable java.util.UUID reservationId) {
        return ReservationResponse.from(reservationService.get(reservationId));
    }
}
