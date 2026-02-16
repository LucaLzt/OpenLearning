package com.projects.openlearning.content.internal.service.command.dto;

import java.util.UUID;

public record CreateSectionCommand(
        UUID courseId,
        String title,
        Integer orderIndex,
        UUID instructorId
) {
}
