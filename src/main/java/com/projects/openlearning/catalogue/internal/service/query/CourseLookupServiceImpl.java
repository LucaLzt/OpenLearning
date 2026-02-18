package com.projects.openlearning.catalogue.internal.service.query;

import com.projects.openlearning.catalogue.api.CourseLookupService;
import com.projects.openlearning.catalogue.internal.repository.CourseProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseLookupServiceImpl implements CourseLookupService {

    private final CourseProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<CoursePriceInfo> getCoursePriceInfo(UUID courseId) {
        log.info("Looking up course price info for course ID: {}", courseId);

        // 1. Fetch the CourseProduct from the repository
        return productRepository.findById(courseId)
                .map(courseProduct -> new CoursePriceInfo(
                        courseProduct.getId(),
                        courseProduct.getPrice(),
                        true
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogueCourseSummary> getCoursesByIds(List<UUID> courseIds) {
        return productRepository.findAllByIdIn(courseIds).stream()
                .map(p -> new CatalogueCourseSummary(
                        p.getId(),
                        p.getTitle(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getInstructorName()
                ))
                .toList();
    }
}
