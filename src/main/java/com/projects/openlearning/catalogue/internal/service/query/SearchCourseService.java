package com.projects.openlearning.catalogue.internal.service.query;

import com.projects.openlearning.catalogue.internal.model.CourseProduct;
import com.projects.openlearning.catalogue.internal.repository.CourseProductRepository;
import com.projects.openlearning.catalogue.internal.service.model.CatalogueItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchCourseService {

    private final CourseProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<CatalogueItem> searchCourses(String query, Pageable pageable) {
        Page<CourseProduct> products;

        if (query == null || query.trim().isEmpty()) {
            // 1. If the query is null or empty, return all courses with pagination
            products = productRepository.findAll(pageable);
        } else {
            // 2. Otherwise, search for courses where the title contains the query string (case-insensitive)
            products = productRepository.findByTitleContainingIgnoreCase(query.trim(), pageable);
        }

        // 3. Map the CourseProduct entities to CatalogueItem DTOs
        return products.map(p -> new CatalogueItem(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getInstructorName(),
                p.getPrice(),
                p.getPublishedAt()
        ));
    }
}
