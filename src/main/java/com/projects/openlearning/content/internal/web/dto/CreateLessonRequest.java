package com.projects.openlearning.content.internal.web.dto;

public record CreateLessonRequest(
        String title,
        String videoUrl,
        String contextText,
        Integer orderIndex
) {
}
