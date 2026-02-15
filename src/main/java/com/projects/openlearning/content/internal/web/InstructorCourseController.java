package com.projects.openlearning.content.internal.web;

import com.projects.openlearning.common.security.api.AuthenticatedUser;
import com.projects.openlearning.content.internal.service.CreateCourseService;
import com.projects.openlearning.content.internal.service.UpdateCourseStatusService;
import com.projects.openlearning.content.internal.service.dto.CreateCourseCommand;
import com.projects.openlearning.content.internal.service.dto.UpdateCourseStatusCommand;
import com.projects.openlearning.content.internal.web.dto.CreateCourseRequest;
import com.projects.openlearning.content.internal.service.dto.ResourceIdResponse;
import com.projects.openlearning.content.internal.web.dto.UpdateCourseStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/instructors/me/courses")
public class InstructorCourseController {

    private final CreateCourseService createCourseService;
    private final UpdateCourseStatusService updateCourseStatusService;

    @PostMapping
    public ResponseEntity<ResourceIdResponse> createCourse(@RequestBody CreateCourseRequest request,
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
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResourceIdResponse(newCourseId));
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
