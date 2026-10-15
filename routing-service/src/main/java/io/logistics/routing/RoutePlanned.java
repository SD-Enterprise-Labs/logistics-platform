package io.logistics.routing;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Event contract for topic route.planned (v1). */
public record RoutePlanned(UUID shipmentId, List<String> legs, Instant plannedAt) {}
