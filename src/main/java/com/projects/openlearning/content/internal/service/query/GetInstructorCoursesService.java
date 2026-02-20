package com.projects.openlearning.content.internal.service.query;

import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.service.model.CourseSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetInstructorCoursesService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<CourseSummary> getCoursesByInstructor(UUID instructorId) {
        log.info("Fetching courses for instructor with id: {}", instructorId);

        // 1. Fetch courses from the repository
        List<CourseSummary> courses = courseRepository.findAllByInstructorIdOrderByUpdatedAtDesc(instructorId)
                .stream()
                .map(CourseSummary::fromEntity)
                .toList();

        log.info("Found {} courses for instructor with id: {}", courses.size(), instructorId);

        // 2. Return the list of course summaries
        return courses;
    }
}
