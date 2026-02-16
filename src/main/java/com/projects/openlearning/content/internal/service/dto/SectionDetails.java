package com.projects.openlearning.content.internal.service.dto;

import com.projects.openlearning.content.internal.model.Section;

import java.util.List;
import java.util.UUID;

public record SectionDetails(
        UUID id,
        UUID courseId,
        String title,
        Integer orderIndex,
        List<LessonDetails> lessons
) {
    public static SectionDetails fromEntity(Section section) {
        return new SectionDetails(
                section.getId(),
                section.getCourse().getId(),
                section.getTitle(),
                section.getOrderIndex(),
                section.getLessons() != null
                        ? section.getLessons().stream().map(LessonDetails::fromEntity).toList()
                        : List.of()
        );
    }
}
