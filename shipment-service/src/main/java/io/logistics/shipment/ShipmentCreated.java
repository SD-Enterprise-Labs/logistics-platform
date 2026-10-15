package io.logistics.shipment;

import java.time.Instant;
import java.util.UUID;

/** Event contract for topic shipment.created (v1). */
public record ShipmentCreated(UUID shipmentId, String origin, String destination,
                              double weightKg, Instant createdAt) {}
