package com.projects.openlearning.catalogue.internal.web;

import com.projects.openlearning.catalogue.internal.service.model.CatalogueCourseDetails;
import com.projects.openlearning.catalogue.internal.service.model.CatalogueItem;
import com.projects.openlearning.catalogue.internal.service.query.GetCourseDetailsService;
import com.projects.openlearning.catalogue.internal.service.query.SearchCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/catalogue")
@RequiredArgsConstructor
public class CatalogueController {

    private final SearchCourseService searchCourseService;
    private final GetCourseDetailsService getCourseDetailsService;

    @GetMapping("/courses")
    public ResponseEntity<Page<CatalogueItem>> search(@RequestParam(required = false) String q,
                                                      @PageableDefault(size = 10, sort = "publishedAt", direction = Sort.Direction.DESC)
                                                      Pageable pageable) {
        log.info("Received search request with query: '{}' and pagination: {}", q, pageable);

        // 1. Call the service layer to perform the search
        Page<CatalogueItem> results = searchCourseService.searchCourses(q, pageable);

        // 2. Return the search results in the response
        return ResponseEntity.ok(results);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CatalogueCourseDetails> getCourseDetails(@PathVariable UUID courseId) {
        log.info("Received request for course details with ID: {}", courseId);

        // 1. Call the service layer to get the course details
        CatalogueCourseDetails details = getCourseDetailsService.getCourseDetails(courseId);

        // 2. Return the course details in the response
        return ResponseEntity.ok(details);
    }
}
