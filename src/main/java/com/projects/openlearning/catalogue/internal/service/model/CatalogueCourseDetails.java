package com.projects.openlearning.catalogue.internal.service.model;

import com.projects.openlearning.catalogue.internal.model.CourseProduct;
import com.projects.openlearning.content.api.dto.PublicSection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CatalogueCourseDetails(
        UUID id,
        String title,
        String description,
        String instructorName,
        BigDecimal price,
        Instant publishedAt,
        List<PublicSection> syllabus
) {
    public static CatalogueCourseDetails fromEntity(CourseProduct entity) {
        return new CatalogueCourseDetails(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getInstructorName(),
                entity.getPrice(),
                entity.getPublishedAt(),
                entity.getSyllabus()
        );
    }
}
