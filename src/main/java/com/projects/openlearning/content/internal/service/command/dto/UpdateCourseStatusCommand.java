package com.projects.openlearning.content.internal.service.command.dto;

import com.projects.openlearning.content.internal.model.CourseStatus;

import java.util.UUID;

public record UpdateCourseStatusCommand(
        UUID courseId,
        CourseStatus newStatus,
        UUID instructorId,
        String instructorName
) {
}
