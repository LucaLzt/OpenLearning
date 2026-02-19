package com.projects.openlearning.content.internal.service.model;

import java.util.UUID;

public record LessonContentResponse(
        UUID id,
        String title,
        String videoUrl,
        String contentText,
        Integer orderIndex
) {
}
