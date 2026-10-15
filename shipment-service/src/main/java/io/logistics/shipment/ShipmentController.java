package io.logistics.shipment;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipments")
class ShipmentController {

    private final ShipmentService service;

    ShipmentController(ShipmentService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ShipmentResponse create(@Valid @RequestBody CreateShipmentRequest req) {
        return ShipmentResponse.from(service.create(req));
    }

    @GetMapping("/{id}")
    ShipmentResponse get(@PathVariable UUID id) {
        return ShipmentResponse.from(service.get(id));
    }
}
