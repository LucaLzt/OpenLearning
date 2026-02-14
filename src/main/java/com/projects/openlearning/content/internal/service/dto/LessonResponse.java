package com.projects.openlearning.content.internal.service.dto;

import com.projects.openlearning.content.internal.model.Lesson;

import java.util.UUID;

public record LessonResponse(
        UUID id,
        UUID sectionId,
        String title,
        String videoUrl,
        String contextText,
        Integer orderIndex
) {
    public static LessonResponse fromEntity(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getSection().getId(),
                lesson.getTitle(),
                lesson.getVideoUrl(),
                lesson.getContextText(),
                lesson.getOrderIndex()
        );
    }
}
