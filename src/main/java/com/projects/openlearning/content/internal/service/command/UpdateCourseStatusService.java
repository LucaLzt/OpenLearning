package com.projects.openlearning.content.internal.service.command;

import com.projects.openlearning.content.internal.model.CourseStatus;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.service.command.dto.UpdateCourseStatusCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCourseStatusService {

    private final CourseRepository courseRepository;

    @Transactional
    public void updateCourseStatus(UpdateCourseStatusCommand command) {
        // 1. Search for the course by ID
        var course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + command.courseId()));

        // 2. Check if the authenticated user is the instructor of the course
        if (!course.getInstructorId().equals(command.instructorId())) {
            throw new IllegalArgumentException("User is not the instructor of the course");
        }

        // 3. Use switch case to determine the new status and update the course accordingly
        switch (command.newStatus()) {
            case CourseStatus.PUBLISHED -> {
                course.archive();
            }
            case CourseStatus.DRAFT -> {
                course.publish();
            }
            default -> throw new IllegalArgumentException("Invalid course status: " + command.newStatus());
        }

        // 4. Save the updated course back to the repository
        courseRepository.save(course);
    }
}
