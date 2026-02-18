package com.projects.openlearning.catalogue.api;

import com.projects.openlearning.catalogue.api.dto.CatalogueCourseSummary;

import java.util.List;
import java.util.UUID;

public interface CourseSummaryApi {

    List<CatalogueCourseSummary> getCoursesByIds(List<UUID> courseIds);

}
