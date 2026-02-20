package com.projects.openlearning.catalogue.internal.service.query;

import com.projects.openlearning.catalogue.internal.exception.CatalogueCourseNotFound;
import com.projects.openlearning.catalogue.internal.repository.CourseProductRepository;
import com.projects.openlearning.catalogue.internal.service.model.CatalogueCourseDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetCourseDetailsService {

    private final CourseProductRepository productRepository;

    @Transactional(readOnly = true)
    public CatalogueCourseDetails getCourseDetails(UUID courseId) {
        log.info("Fetching course details for course ID: {}", courseId);

        // 1. Fetch the CourseProduct from the repository
        var courseProduct = productRepository.findById(courseId)
                .orElseThrow(() -> new CatalogueCourseNotFound(courseId));

        // 2. Map the CourseProduct to CatalogueCourseDetails and return
        return CatalogueCourseDetails.fromEntity(courseProduct);
    }
}
