package com.projects.openlearning.enrollment.internal.service.query;

import com.projects.openlearning.catalogue.api.CourseSummaryApi;
import com.projects.openlearning.catalogue.api.dto.CatalogueCourseSummary;
import com.projects.openlearning.enrollment.internal.model.Enrollment;
import com.projects.openlearning.enrollment.internal.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetEnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseSummaryApi courseLookupService;

    @Transactional(readOnly = true)
    public List<CatalogueCourseSummary> getEnrolledCoursesForStudent(UUID studentId) {
        log.info("Fetching enrolled courses for student ID: {}", studentId);

        // 1. Fetch all enrollments for the given student ID
        List<UUID> courseIds = enrollmentRepository.findAllByUserId(studentId)
                .stream()
                .map(Enrollment::getCourseId)
                .toList();

        // 2. If no enrollments found, return an empty list
        if (courseIds.isEmpty()) {
            log.info("No enrollments found for student ID: {}", studentId);
            return List.of();
        }

        // 3. Fetch course details for the enrolled course IDs using the CourseLookupService
        return courseLookupService.getCoursesByIds(courseIds);
    }
}
