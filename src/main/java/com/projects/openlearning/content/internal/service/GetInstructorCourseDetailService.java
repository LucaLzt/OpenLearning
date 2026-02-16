package com.projects.openlearning.content.internal.service;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.service.dto.CourseDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetInstructorCourseDetailService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public CourseDetails getCourseDetailsByInstructor(UUID instructorId, UUID courseId) {
        log.info("Fetching course details for course with id: {} and instructor with id: {}", courseId, instructorId);

        // 1. Fetch the course from the repository
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        // 2. Validate if the instructor owns the course
        if (!course.getInstructorId().equals(instructorId)) {
            throw new IllegalArgumentException("Instructor does not own the course with id: " + courseId);
        }

        // 3. Return the course details
        return CourseDetails.fromEntity(course);
    }

}
