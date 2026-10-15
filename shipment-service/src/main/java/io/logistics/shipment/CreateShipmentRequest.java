package io.logistics.shipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateShipmentRequest(
        @NotBlank String origin,
        @NotBlank String destination,
        @Positive double weightKg) {}
