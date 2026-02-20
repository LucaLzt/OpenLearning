package com.projects.openlearning.content.internal.service.query;

import com.projects.openlearning.content.api.CourseAccessValidator;
import com.projects.openlearning.content.internal.exception.LessonNotFoundException;
import com.projects.openlearning.content.internal.exception.StudentEnrollmentException;
import com.projects.openlearning.content.internal.model.Lesson;
import com.projects.openlearning.content.internal.repository.LessonRepository;
import com.projects.openlearning.content.internal.service.model.LessonContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetLessonContentService {

    private final LessonRepository lessonRepository;
    private final CourseAccessValidator courseAccessValidator;

    @Transactional(readOnly = true)
    public LessonContentResponse getLessonContent(UUID lessonId, UUID studentId) {
        log.info("Getting lesson content for lesson with id {}", lessonId);

        // 1. Fetch the lesson from the repository
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException(lessonId));

        // 2. Check if the student has access to the course associated with the lesson
        UUID courseId = lesson.getSection().getCourse().getId();

        log.info("Checking access for student {} to course {}", studentId, courseId);
        if (!courseAccessValidator.hasActiveAccess(studentId, courseId)) {
            log.warn("Student {} has no access to course {}", studentId, courseId);
            throw new StudentEnrollmentException(courseId);
        }

        // 3. Return the lesson content in the response
        return new LessonContentResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getVideoUrl(),
                lesson.getContextText(),
                lesson.getOrderIndex()
        );
    }
}
