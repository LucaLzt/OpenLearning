package com.projects.openlearning.content.internal.service.model;

import com.projects.openlearning.content.internal.model.Lesson;

import java.util.UUID;

public record LessonDetails(
        UUID id,
        UUID sectionId,
        String title,
        String videoUrl,
        String contextText,
        Integer orderIndex
) {
    public static LessonDetails fromEntity(Lesson lesson) {
        return new LessonDetails(
                lesson.getId(),
                lesson.getSection().getId(),
                lesson.getTitle(),
                lesson.getVideoUrl(),
                lesson.getContextText(),
                lesson.getOrderIndex()
        );
    }
}
