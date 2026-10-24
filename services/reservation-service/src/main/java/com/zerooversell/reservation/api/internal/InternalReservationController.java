package com.zerooversell.reservation.api.internal;

import com.zerooversell.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1")
public class InternalReservationController {
    private final ReservationService reservationService;

    public InternalReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/inventory")
    public ProvisionResponse provision(@Valid @RequestBody InventoryProvisionRequest request) {
        int inserted = reservationService.provisionInventory(request.saleId(), request.quantity(),
                request.unitPrice(), request.currency());
        return new ProvisionResponse(inserted == 1 ? "PROVISIONED" : "ALREADY_EXISTS");
    }

    @PostMapping("/reservations/expire-due")
    public ExpiryResponse expireDue() {
        return new ExpiryResponse(reservationService.expireDueReservations());
    }

    public record ProvisionResponse(String result) {}
    public record ExpiryResponse(int expiredReservations) {}
}
