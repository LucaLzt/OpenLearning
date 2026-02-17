package com.projects.openlearning.content.api.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CoursePublishedEvent(
        UUID courseId,
        String title,
        String description,
        BigDecimal price,
        String instructorName,
        // String coverUrl, // TODO: Agregar URL de portada cuando esté disponible
        Instant publishedAt
) {
}
