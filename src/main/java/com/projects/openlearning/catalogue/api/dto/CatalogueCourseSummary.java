package com.projects.openlearning.catalogue.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CatalogueCourseSummary(
        UUID id,
        String title,
        String description,
        BigDecimal price,
        String instructorName
) {
}
