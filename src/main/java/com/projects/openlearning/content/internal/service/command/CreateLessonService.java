package com.projects.openlearning.content.internal.service.command;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.model.Lesson;
import com.projects.openlearning.content.internal.model.Section;
import com.projects.openlearning.content.internal.repository.CourseRepository;
import com.projects.openlearning.content.internal.repository.LessonRepository;
import com.projects.openlearning.content.internal.service.command.dto.CreateLessonCommand;
import com.projects.openlearning.content.internal.service.model.LessonDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateLessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public LessonDetails createLesson(CreateLessonCommand command) {
        log.info("Creating lesson with title: {}, sectionId: {}, orderIndex: {}",
                command.title(), command.sectionId(), command.orderIndex());

        // 1. Search for the course in the database
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        // 2. Validate if the instructor owns the course
        if (!course.getInstructorId().equals(command.instructorId())) {
            throw new IllegalArgumentException("Instructor does not own the course with id: " + command.courseId());
        }

        // 3. Search fot he section in the course
        Section section = course.getSections().stream()
                .filter(s -> s.getId().equals(command.sectionId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Section not found with id: " + command.sectionId()));

        // 4. Create a new lesson using the factory method
        Lesson lesson = Lesson.createNewLesson(
                section,
                command.title(),
                command.videoUrl(),
                command.contextText(),
                command.orderIndex()
        );

        // 5. Use the helper method to add the lesson to the section
        section.addLesson(lesson);

        // 6. Save the lesson and the course
        Lesson newLesson = lessonRepository.save(lesson);
        courseRepository.save(course);

        // 7. Return the created lesson details
        return LessonDetails.fromEntity(newLesson);
    }
}
