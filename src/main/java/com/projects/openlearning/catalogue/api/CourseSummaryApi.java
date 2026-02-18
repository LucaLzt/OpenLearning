package com.projects.openlearning.catalogue.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseLookupService {

    Optional<CoursePriceInfo> getCoursePriceInfo(UUID courseId);

    record CoursePriceInfo(UUID id, BigDecimal price, boolean isPublished) {}

    List<CatalogueCourseSummary> getCoursesByIds(List<UUID> courseIds);

    record CatalogueCourseSummary(
            UUID id,
            String title,
            String description,
            BigDecimal price,
            String instructorName
    ) {}

}
