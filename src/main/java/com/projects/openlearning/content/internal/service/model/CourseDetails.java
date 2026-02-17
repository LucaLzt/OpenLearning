package com.projects.openlearning.content.internal.service.model;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.model.CourseStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CourseDetails (
        UUID id,
        UUID instructorId,
        String title,
        String description,
        CourseStatus status,
        BigDecimal price,
        Instant lastUpdated,
        Boolean isPublished,
        List<SectionDetails> sections
) {
    public static CourseDetails fromEntity(Course course) {
        return new CourseDetails(
                course.getId(),
                course.getInstructorId(),
                course.getTitle(),
                course.getDescription(),
                course.getStatus(),
                course.getPrice() != null ? course.getPrice() : BigDecimal.ZERO,
                course.getUpdatedAt(),
                course.getStatus() == CourseStatus.PUBLISHED,
                course.getSections() != null
                    ? course.getSections().stream()
                            .map(SectionDetails::fromEntity)
                            .toList()
                    : List.of()
        );
    }
}
