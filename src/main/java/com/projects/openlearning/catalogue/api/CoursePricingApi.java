package com.projects.openlearning.catalogue.api;

import com.projects.openlearning.catalogue.api.dto.CoursePriceInfo;

import java.util.Optional;
import java.util.UUID;

public interface CoursePricingApi {

    Optional<CoursePriceInfo> getCoursePriceInfo(UUID courseId);

}
