package com.zerooversell.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateReservationRequest(@NotNull UUID saleId, @NotNull UUID customerId) {}
