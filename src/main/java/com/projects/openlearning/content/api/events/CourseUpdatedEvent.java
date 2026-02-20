package com.projects.openlearning.content.api.events;

import java.math.BigDecimal;
import java.util.UUID;

public record CourseUpdatedEvent(
        UUID courseId,
        String title,
        String description,
        BigDecimal price
) {
}
