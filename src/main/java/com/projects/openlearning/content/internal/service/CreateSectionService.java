package com.projects.openlearning.content.internal.service;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.model.Section;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.repository.SectionRepository;
import com.projects.openlearning.content.internal.service.dto.CreateSectionCommand;
import com.projects.openlearning.content.internal.service.dto.SectionDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public SectionDetails createSection(CreateSectionCommand command) {
        log.info("Creating section with title: {}, courseId: {}, orderIndex: {}",
                command.title(), command.courseId(), command.orderIndex());

        // 1. Search for the course in the database
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        // 2. Validate if the instructor owns the course
        if (!course.getInstructorId().equals(command.instructorId())) {
            throw new IllegalArgumentException("Instructor does not own the course with id: " + command.courseId());
        }

        // 3. Create a new section using the factory method
        Section section = Section.createNewSection(
                course,
                command.title(),
                command.orderIndex()
        );

        // 4. Use the helper method to add the section to the course
        course.addSection(section);

        // 5. Save the section and the course
        Section savedSection = sectionRepository.save(section);
        courseRepository.save(course);

        // 6. Return the created section details
        return SectionDetails.fromEntity(savedSection);
    }
}
