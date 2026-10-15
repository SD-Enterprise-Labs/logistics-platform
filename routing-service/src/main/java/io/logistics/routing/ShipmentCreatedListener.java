package io.logistics.routing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Day-1 version: stateless, naive routing (origin -> hub -> destination).
 * TODO (day 2): idempotency via processed-event table, retry topic + DLT, real route logic.
 */
@Component
class ShipmentCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(ShipmentCreatedListener.class);

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;

    ShipmentCreatedListener(KafkaTemplate<String, String> kafka, ObjectMapper mapper) {
        this.kafka = kafka;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "shipment.created", groupId = "routing-service")
    void onShipmentCreated(ConsumerRecord<String, String> record) throws Exception {
        JsonNode event = mapper.readTree(record.value());
        UUID shipmentId = UUID.fromString(event.get("shipmentId").asText());
        String origin = event.get("origin").asText();
        String destination = event.get("destination").asText();

        var planned = new RoutePlanned(shipmentId,
                List.of(origin, "CENTRAL-HUB", destination), Instant.now());
        kafka.send("route.planned", shipmentId.toString(), mapper.writeValueAsString(planned));
        log.info("Planned route for shipment {}: {} -> CENTRAL-HUB -> {}", shipmentId, origin, destination);
    }
}
