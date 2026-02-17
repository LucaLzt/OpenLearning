package com.projects.openlearning.content.internal.web.dto;

import java.math.BigDecimal;

public record UpdateCourseRequest(
        String title,
        String description,
        BigDecimal price
) {
}
