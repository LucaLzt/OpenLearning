package com.projects.openlearning.content.internal.service;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.service.dto.CreateCourseCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public UUID createCourse(CreateCourseCommand command) {
        log.info("Creating course with title: {}, description: {}, instructorId: {}",
                command.title(), command.description(), command.instructorId());

        // 1. Create a new Course entity
        Course course = Course.createNewCourse(command.instructorId(), command.title(), command.description());

        // 2. Save the course to the database
        courseRepository.save(course);

        log.info("Created course with id: {}", course.getId());

        // 3. Return the ID of the newly created course
        return course.getId();
    }
}
