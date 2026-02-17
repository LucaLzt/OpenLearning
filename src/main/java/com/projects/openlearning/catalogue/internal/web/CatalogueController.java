package com.projects.openlearning.catalogue.internal.web;

import com.projects.openlearning.catalogue.internal.model.CourseProduct;
import com.projects.openlearning.catalogue.internal.service.model.CatalogueItem;
import com.projects.openlearning.catalogue.internal.service.query.SearchCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalogue")
@RequiredArgsConstructor
public class CatalogueController {

    private final SearchCourseService searchCourseService;

    @GetMapping("/courses")
    public ResponseEntity<Page<CatalogueItem>> search(@RequestParam(required = false) String q,
                                                      @PageableDefault(size = 10, sort = "publishedAt", direction = Sort.Direction.DESC)
                                                      Pageable pageable) {
        Page<CatalogueItem> results = searchCourseService.searchCourses(q, pageable);
        return ResponseEntity.ok(results);
    }
}
