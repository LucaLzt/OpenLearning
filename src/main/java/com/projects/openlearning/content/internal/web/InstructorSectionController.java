package com.projects.openlearning.content.internal.web;

import com.projects.openlearning.common.security.api.AuthenticatedUser;
import com.projects.openlearning.content.internal.service.command.CreateSectionService;
import com.projects.openlearning.content.internal.service.command.dto.CreateSectionCommand;
import com.projects.openlearning.content.internal.service.model.SectionDetails;
import com.projects.openlearning.content.internal.web.dto.CreateSectionRequest;
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
@RequestMapping("/api/v1/instructors/me/courses/{courseId}/sections")
public class InstructorSectionController {

    private final CreateSectionService createSectionService;

    @PostMapping
    public ResponseEntity<SectionDetails> createSection(@RequestBody CreateSectionRequest request,
                                                        @PathVariable UUID courseId,
                                                        @AuthenticationPrincipal AuthenticatedUser authUser) {
        log.info("Creating section: {}", request.title());

        // 1. Create the command object for the service layer
        var command = new CreateSectionCommand(
                courseId,
                request.title(),
                request.orderIndex(),
                authUser.getUserId()
        );

        // 2. Call the service layer to create the section
        SectionDetails response = createSectionService.createSection(command);

        // 3. Return a 201 Created response
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
