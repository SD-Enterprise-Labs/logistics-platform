package io.logistics.shipment;

import java.util.UUID;

public record ShipmentResponse(UUID id, String origin, String destination, String status) {
    static ShipmentResponse from(Shipment s) {
        return new ShipmentResponse(s.getId(), s.getOrigin(), s.getDestination(), s.getStatus().name());
    }
}
