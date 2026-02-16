package com.projects.openlearning.content.internal.service.model;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.model.CourseStatus;

import java.time.Instant;
import java.util.UUID;

public record CourseSummary(
        UUID id,
        String title,
        String description,
        CourseStatus status,
        Instant lastUpdated,
        Integer sectionCount,
        Boolean isPublished
) {
    public static CourseSummary fromEntity(Course course) {
        return new CourseSummary(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getStatus(),
                course.getUpdatedAt(),
                course.getSections() != null ? course.getSections().size() : 0,
                course.getStatus() == CourseStatus.PUBLISHED
        );
    }
}
