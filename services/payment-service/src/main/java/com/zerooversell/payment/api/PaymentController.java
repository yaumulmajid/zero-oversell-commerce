package com.zerooversell.payment.api;

import com.zerooversell.payment.domain.Payment;
import com.zerooversell.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService service;
    public PaymentController(PaymentService service) { this.service = service; }

    @GetMapping("/{reservationId}")
    public PaymentResponse get(@PathVariable UUID reservationId) {
        return service.findByReservation(reservationId).map(PaymentResponse::from)
                .orElseThrow(() -> new com.zerooversell.payment.service.PaymentNotFoundException(reservationId));
    }

    /** Demo-only payment callback. Replace with a signed provider webhook adapter. */
    @PostMapping("/{reservationId}/demo-confirm")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PaymentResponse confirm(@PathVariable UUID reservationId) {
        return PaymentResponse.from(service.confirm(reservationId));
    }

    public record PaymentResponse(UUID reservationId, BigDecimal amount, String currency,
                                  String providerReference, String status, Instant expiresAt) {
        static PaymentResponse from(Payment p) { return new PaymentResponse(p.getReservationId(), p.getAmount(),
                p.getCurrency(), p.getProviderReference(), p.getStatus().name(), p.getExpiresAt()); }
    }
}
