package io.logistics.shipment;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Polling outbox relay. Delivery is at-least-once: if we crash after send but before commit,
 * the event is re-sent, so consumers must be idempotent.
 * Keyed by shipment id so all events of one shipment stay in order (same partition).
 */
@Component
class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxRepository outbox;
    private final KafkaTemplate<String, String> kafka;

    OutboxPublisher(OutboxRepository outbox, KafkaTemplate<String, String> kafka) {
        this.outbox = outbox;
        this.kafka = kafka;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    void publishPending() {
        for (OutboxEvent e : outbox.findTop50ByPublishedAtIsNullOrderByCreatedAt()) {
            try {
                kafka.send(e.getTopic(), e.getAggregateId(), e.getPayload()).get(5, TimeUnit.SECONDS);
                e.markPublished();
            } catch (Exception ex) {
                log.warn("Outbox publish failed for {}, will retry: {}", e.getAggregateId(), ex.toString());
                return; // keep ordering: stop at first failure
            }
        }
    }
}
