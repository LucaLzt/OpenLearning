package com.projects.openlearning.content.internal.service.command;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.service.command.dto.UpdateCourseCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public void updateCourse(UpdateCourseCommand command) {
        log.info("Updating course with id: {}", command.courseId());

        // 1. Fetch the course from the repository
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        // 2. Validate if the authenticated user is the instructor of the course
        if (!course.getInstructorId().equals(command.instructorId())) {
            throw new IllegalArgumentException("Instructor does not own the course with id: " + command.courseId());
        }

        // 3. Update the course details
        course.updateCourseDetails(command.title(), command.description(), command.price());

        // 4. Save the updated course back to the repository
        courseRepository.save(course);
    }
}
