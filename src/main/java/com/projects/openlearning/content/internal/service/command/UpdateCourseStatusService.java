package com.projects.openlearning.content.internal.service.command;

import com.projects.openlearning.content.api.events.CourseArchivedEvent;
import com.projects.openlearning.content.api.events.CoursePublishedEvent;
import com.projects.openlearning.content.api.events.dto.PublicLesson;
import com.projects.openlearning.content.api.events.dto.PublicSection;
import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.model.CourseStatus;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.service.command.dto.UpdateCourseStatusCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCourseStatusService {

    private final CourseRepository courseRepository;
    private final ApplicationEventPublisher publisher;

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
            case PUBLISHED -> handlePublishing(course, command.instructorName());
            case DRAFT -> handleArchiving(course);
            default -> throw new IllegalArgumentException("Invalid transition to status: " + command.newStatus());
        }

        // 4. Save the updated course back to the repository
        courseRepository.save(course);
    }

    private void handlePublishing(Course course, String instructorName) {
        log.info("Attempting to publish course with ID: {}", course.getId());

        // 1. Publish the course (Internal: validate status, price, etc.)
        course.publish();

        // 2. Map the syllabus to the event
        List<PublicSection> publicSyllabus = course.getSections().stream()
                .map(s -> new PublicSection(
                        s.getTitle(),
                        s.getOrderIndex(),
                        s.getLessons().stream()
                                .map(l -> new PublicLesson(
                                        l.getTitle(),
                                        l.getOrderIndex()
                                ))
                                .toList()
                ))
                .toList();

        // 3. Create and publish CoursePublishedEvent
        var event = new CoursePublishedEvent(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                instructorName,
                // "Cover URL Placeholder", // TODO: Agregar URL de portada cuando esté disponible
                course.getUpdatedAt(),
                publicSyllabus
        );
        publisher.publishEvent(event);

        log.info("Course with ID: {} published successfully", course.getId());
    }

    private void handleArchiving(Course course) {
        log.info("Archiving course: {}", course.getId());
        course.archive();

        // 2. Create and public CourseArchivedEvent
        var event = new CourseArchivedEvent(
                course.getId()
        );
        publisher.publishEvent(event);

        log.info("Course with ID: {} archived successfully", course.getId());
    }
}
