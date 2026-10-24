package com.zerooversell.reservation.api.internal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import java.math.BigDecimal;

public record InventoryProvisionRequest(@NotNull UUID saleId, @Min(0) int quantity,
                                        @NotNull @jakarta.validation.constraints.DecimalMin("0.01") BigDecimal unitPrice,
                                        @NotNull String currency) {}
