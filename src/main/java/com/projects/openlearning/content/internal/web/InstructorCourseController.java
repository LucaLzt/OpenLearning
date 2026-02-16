package com.projects.openlearning.content.internal.web;

import com.projects.openlearning.common.security.api.AuthenticatedUser;
import com.projects.openlearning.content.internal.service.command.CreateCourseService;
import com.projects.openlearning.content.internal.service.command.UpdateCourseService;
import com.projects.openlearning.content.internal.service.command.dto.UpdateCourseCommand;
import com.projects.openlearning.content.internal.service.model.CourseDetails;
import com.projects.openlearning.content.internal.service.model.CourseSummary;
import com.projects.openlearning.content.internal.service.model.ResourceCreated;
import com.projects.openlearning.content.internal.service.query.GetInstructorCourseDetailService;
import com.projects.openlearning.content.internal.service.query.GetInstructorCoursesService;
import com.projects.openlearning.content.internal.service.command.UpdateCourseStatusService;
import com.projects.openlearning.content.internal.service.command.dto.CreateCourseCommand;
import com.projects.openlearning.content.internal.service.command.dto.UpdateCourseStatusCommand;
import com.projects.openlearning.content.internal.web.dto.CreateCourseRequest;
import com.projects.openlearning.content.internal.web.dto.UpdateCourseRequest;
import com.projects.openlearning.content.internal.web.dto.UpdateCourseStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/instructors/me/courses")
public class InstructorCourseController {

    private final CreateCourseService createCourseService;
    private final GetInstructorCoursesService getInstructorCoursesService;
    private final GetInstructorCourseDetailService getInstructorCourseDetailService;
    private final UpdateCourseService updateCourseService;
    private final UpdateCourseStatusService updateCourseStatusService;

    @PostMapping
    public ResponseEntity<ResourceCreated> createCourse(@RequestBody CreateCourseRequest request,
                                                        @AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Received request to create course with title: {}, description: {}",
                request.title(), request.description());

        // 1. Call the service layer to create the course
        UUID newCourseId = createCourseService.createCourse(new CreateCourseCommand(
                        request.title(),
                        request.description(),
                        authUser.getUserId()
                )
        );

        // 2. Return a 201 Created response
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResourceCreated(newCourseId));
    }

    @GetMapping
    public ResponseEntity<List<CourseSummary>> getMyCourses(@AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Received request to get courses for instructor with id: {}", authUser.getUserId());

        // 1. Call the service layer to get the list of courses
        List<CourseSummary> courses = getInstructorCoursesService.getCoursesByInstructor(authUser.getUserId());

        // 2. Return the list of courses
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetails> getMyCourseDetail(@PathVariable UUID courseId,
                                                           @AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Received request to get course details for course with id: {}", courseId);

        // 1. Call the service layer to get the course details
        CourseDetails courseDetails = getInstructorCourseDetailService.getCourseDetailsByInstructor(authUser.getUserId(), courseId);

        // 2. Return the course details
        return ResponseEntity.ok(courseDetails);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Void> updateCourse(@PathVariable UUID courseId,
                                             @RequestBody UpdateCourseRequest request,
                                             @AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Received request to update course with id: {} - Title: {}", courseId, request.title());

        // 1. Call the service layer to update the course
        updateCourseService.updateCourse(new UpdateCourseCommand(
                        courseId,
                        authUser.getUserId(),
                        request.title(),
                        request.description()
                )
        );

        // 2. Return a 204 No Content response
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{courseId}/status")
    public ResponseEntity<Void> updateCourseStatus(@PathVariable UUID courseId,
                                                   @RequestBody UpdateCourseStatusRequest request,
                                                   @AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Received request to update course with id: {}", courseId);

        // 1. Call the service layer to update the course
        updateCourseStatusService.updateCourseStatus(new UpdateCourseStatusCommand(
                        courseId,
                        request.status(),
                        authUser.getUserId()
                )
        );

        // 2. Return a 204 No Content response
        return ResponseEntity.noContent().build();
    }
}
