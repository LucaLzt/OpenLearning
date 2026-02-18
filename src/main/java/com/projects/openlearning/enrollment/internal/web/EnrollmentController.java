package com.projects.openlearning.enrollment.internal.web;

import com.projects.openlearning.catalogue.api.CourseLookupService.CatalogueCourseSummary;
import com.projects.openlearning.common.security.api.AuthenticatedUser;
import com.projects.openlearning.enrollment.internal.service.command.EnrollStudentService;
import com.projects.openlearning.enrollment.internal.service.command.dto.EnrollStudentCommand;
import com.projects.openlearning.enrollment.internal.service.query.GetEnrollmentService;
import com.projects.openlearning.enrollment.internal.web.dto.EnrollRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/enrollments/me")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollStudentService enrollStudentService;
    private final GetEnrollmentService getEnrollmentService;

    @PostMapping
    public ResponseEntity<Void> enrollToCourse(@RequestBody EnrollRequest request,
                                               @AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Received enrollment request for course ID: {} from user: {}", request.courseId(), authUser.getUserId());

        // 1. Call the service layer to perform the enrollment
        enrollStudentService.enroll(new EnrollStudentCommand(
                        request.courseId(),
                        authUser.getUserId()
                )
        );

        // 2. Return a response indicating successful enrollment
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CatalogueCourseSummary>> getMyEnrollments(@AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Received enrollment request for user: {}", authUser.getUserId());

        // 1. Call the service layer to fetch the enrolled courses for the authenticated user
        List<CatalogueCourseSummary> courses = getEnrollmentService
                .getEnrolledCoursesForStudent(authUser.getUserId());

        // 2. Return the list of enrolled courses in the response
        return ResponseEntity.ok(courses);
    }

}
