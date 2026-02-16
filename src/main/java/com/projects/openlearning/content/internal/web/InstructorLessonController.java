package com.projects.openlearning.content.internal.web;

import com.projects.openlearning.common.security.api.AuthenticatedUser;
import com.projects.openlearning.content.internal.service.command.CreateLessonService;
import com.projects.openlearning.content.internal.service.command.dto.CreateLessonCommand;
import com.projects.openlearning.content.internal.service.model.LessonDetails;
import com.projects.openlearning.content.internal.web.dto.CreateLessonRequest;
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
@RequestMapping("/api/v1/instructors/me/courses/{courseId}/sections/{sectionId}/lessons")
public class InstructorLessonController {

    private final CreateLessonService createLessonService;

    @PostMapping
    public ResponseEntity<LessonDetails> createLesson(@RequestBody CreateLessonRequest request,
                                                      @PathVariable UUID courseId,
                                                      @PathVariable UUID sectionId,
                                                      @AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Creating lesson: {} in section: {}", request.title(), sectionId);

        // 1. Create the command object for the service layer
        var command = new CreateLessonCommand(
                request.title(),
                request.videoUrl(),
                request.contextText(),
                request.orderIndex(),
                courseId,
                sectionId,
                authUser.getUserId()
        );

        // 2. Call the service layer to create the lesson
        LessonDetails response = createLessonService.createLesson(command);

        // 3. Return a 201 Created response
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
