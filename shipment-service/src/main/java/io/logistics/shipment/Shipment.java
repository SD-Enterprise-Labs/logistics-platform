package io.logistics.shipment;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipments")
public class Shipment {

    public enum Status { CREATED, ROUTE_PLANNED, IN_TRANSIT, DELIVERED, FAILED }

    @Id
    private UUID id;
    private String origin;
    private String destination;
    private double weightKg;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant createdAt;

    protected Shipment() {}

    public Shipment(String origin, String destination, double weightKg) {
        this.id = UUID.randomUUID();
        this.origin = origin;
        this.destination = destination;
        this.weightKg = weightKg;
        this.status = Status.CREATED;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public double getWeightKg() { return weightKg; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
