package com.projects.openlearning.enrollment.internal.web.dto;

import java.util.UUID;

public record EnrollRequest(
        UUID courseId
) {
}
