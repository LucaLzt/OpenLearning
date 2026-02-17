package com.projects.openlearning.catalogue.internal.service.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CatalogueItem(
        UUID id,
        String title,
        String description,
        String instructorName,
        BigDecimal price,
        Instant publishedAt
) {
}
