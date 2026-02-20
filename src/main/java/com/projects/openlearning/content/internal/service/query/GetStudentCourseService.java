package com.projects.openlearning.content.internal.service.query;

import com.projects.openlearning.content.api.CourseAccessValidator;
import com.projects.openlearning.content.internal.exception.CourseNotFoundException;
import com.projects.openlearning.content.internal.exception.StudentEnrollmentException;
import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.service.model.StudentCourseView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetStudentCourseService {

    private final CourseRepository courseRepository;
    private final CourseAccessValidator courseAccessValidator;

    @Transactional(readOnly = true)
    public StudentCourseView getCourseForStudent(UUID courseId, UUID studentId) {
        log.info("Getting course for student {} ", studentId);

        // 1. SPI validate if the student has access to the course
        if (!courseAccessValidator.hasActiveAccess(studentId, courseId)) {
            log.warn("Student {} has no access to course {}", studentId, courseId);
            throw new StudentEnrollmentException(courseId);
        }

        // 2. Fetch the course details from the repository
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        // 3. Map and return the course details in the response
        return StudentCourseView.fromEntity(course);
    }
}
