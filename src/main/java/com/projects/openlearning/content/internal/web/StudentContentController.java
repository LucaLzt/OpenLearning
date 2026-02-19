package com.projects.openlearning.content.internal.web;

import com.projects.openlearning.common.security.api.AuthenticatedUser;
import com.projects.openlearning.content.internal.service.model.LessonContentResponse;
import com.projects.openlearning.content.internal.service.model.StudentCourseView;
import com.projects.openlearning.content.internal.service.query.GetLessonContentService;
import com.projects.openlearning.content.internal.service.query.GetStudentCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students/me")
@RequiredArgsConstructor
public class StudentContentController {

    private final GetLessonContentService getLessonContentService;
    private final GetStudentCourseService getStudentCourseService;

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<StudentCourseView> getCoursePlayerConfig(@PathVariable UUID courseId,
                                                                   @AuthenticationPrincipal AuthenticatedUser authUser) {
        StudentCourseView response = getStudentCourseService
                .getCourseForStudent(courseId, authUser.getUserId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonContentResponse> getLessonContent(
            @PathVariable UUID lessonId,
            @AuthenticationPrincipal AuthenticatedUser authUser
    ) {
        LessonContentResponse response = getLessonContentService
                .getLessonContent(lessonId, authUser.getUserId());

        return ResponseEntity.ok(response);
    }

}
