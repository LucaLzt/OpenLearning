package com.projects.openlearning.enrollment.internal.service.command.dto;

import java.util.UUID;

public record EnrollStudentCommand(
        UUID courseId,
        UUID studentId
) {
}
