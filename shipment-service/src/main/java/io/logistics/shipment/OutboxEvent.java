package io.logistics.shipment;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/** Transactional outbox row: written in the same DB transaction as the business change. */
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    private UUID id;
    private String aggregateId;
    private String topic;
    @Column(columnDefinition = "text")
    private String payload;
    private Instant createdAt;
    private Instant publishedAt;

    protected OutboxEvent() {}

    public OutboxEvent(String aggregateId, String topic, String payload) {
        this.id = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.topic = topic;
        this.payload = payload;
        this.createdAt = Instant.now();
    }

    public String getAggregateId() { return aggregateId; }
    public String getTopic() { return topic; }
    public String getPayload() { return payload; }
    public void markPublished() { this.publishedAt = Instant.now(); }
}
