package com.projects.openlearning.content.internal.service.dto;

import com.projects.openlearning.content.internal.model.Section;

import java.util.List;
import java.util.UUID;

public record SectionResponse(
        UUID id,
        UUID courseId,
        String title,
        Integer orderIndex,
        List<LessonResponse> lessons
) {
    public static SectionResponse fromEntity(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getCourse().getId(),
                section.getTitle(),
                section.getOrderIndex(),
                section.getLessons() != null
                        ? section.getLessons().stream().map(LessonResponse::fromEntity).toList()
                        : List.of()
        );
    }
}
