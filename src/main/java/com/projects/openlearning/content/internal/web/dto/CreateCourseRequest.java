package com.projects.openlearning.content.internal.web.dto;

import java.math.BigDecimal;

public record CreateCourseRequest(
        String title,
        String description,
        BigDecimal price
) {
}
