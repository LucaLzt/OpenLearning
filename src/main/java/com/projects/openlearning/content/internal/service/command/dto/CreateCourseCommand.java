package com.projects.openlearning.content.internal.service.command.dto;

import java.util.UUID;

public record CreateCourseCommand(
        String title,
        String description,
        UUID instructorId
) {
}
