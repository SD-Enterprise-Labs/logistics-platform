package io.logistics.shipment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ShipmentService {

    static final String TOPIC_SHIPMENT_CREATED = "shipment.created";

    private final ShipmentRepository shipments;
    private final OutboxRepository outbox;
    private final ObjectMapper mapper;

    public ShipmentService(ShipmentRepository shipments, OutboxRepository outbox, ObjectMapper mapper) {
        this.shipments = shipments;
        this.outbox = outbox;
        this.mapper = mapper;
    }

    /** Business row and outbox row commit atomically; the publisher ships the event later. */
    @Transactional
    public Shipment create(CreateShipmentRequest req) {
        Shipment s = shipments.save(new Shipment(req.origin(), req.destination(), req.weightKg()));
        var event = new ShipmentCreated(s.getId(), s.getOrigin(), s.getDestination(),
                s.getWeightKg(), s.getCreatedAt());
        outbox.save(new OutboxEvent(s.getId().toString(), TOPIC_SHIPMENT_CREATED, toJson(event)));
        return s;
    }

    @Transactional(readOnly = true)
    public Shipment get(UUID id) {
        return shipments.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));
    }

    private String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
