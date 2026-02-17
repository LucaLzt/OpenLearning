package com.projects.openlearning.content.internal.service.command.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateCourseCommand(
        UUID courseId,
        UUID instructorId,
        String title,
        String description,
        BigDecimal price
) {
}
