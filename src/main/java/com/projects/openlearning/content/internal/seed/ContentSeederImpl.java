package com.projects.openlearning.content.internal.seed;

import com.projects.openlearning.content.api.ContentSeederApi;
import com.projects.openlearning.content.internal.model.CourseStatus;
import com.projects.openlearning.content.internal.service.command.CreateCourseService;
import com.projects.openlearning.content.internal.service.command.CreateLessonService;
import com.projects.openlearning.content.internal.service.command.CreateSectionService;
import com.projects.openlearning.content.internal.service.command.UpdateCourseStatusService;
import com.projects.openlearning.content.internal.service.command.dto.CreateCourseCommand;
import com.projects.openlearning.content.internal.service.command.dto.CreateLessonCommand;
import com.projects.openlearning.content.internal.service.command.dto.CreateSectionCommand;
import com.projects.openlearning.content.internal.service.command.dto.UpdateCourseStatusCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContentSeederImpl implements ContentSeederApi {

    private final CreateCourseService createCourseService;
    private final CreateSectionService createSectionService;
    private final CreateLessonService createLessonService;
    private final UpdateCourseStatusService updateCourseStatusService;

    @Override
    public void seedRandomCourses(UUID instructorId, String instructorName, int numberOfCourses) {

        // 1. Initialize Faker for generating random data
        Faker faker = new Faker();
        log.info("Generating {} random courses for instructor {} ({})", numberOfCourses, instructorName, instructorId);

        // 2. Loop to create the specified number of courses
        for (int i = 0; i < numberOfCourses; i++) {

            // 3. Generate a random course
            String title = faker.educator().course();
            String description = faker.lorem().paragraph(3);
            BigDecimal price = BigDecimal.valueOf(faker.number().randomDouble(2, 10, 150));

            log.info("   -> Creating course: {} (Price: ${})", title, price);

            UUID courseId = createCourseService.createCourse(
                    new CreateCourseCommand(title, description, price, instructorId)
            );

            // 4. For each course, create a random number of sections
            int numSections = faker.number().numberBetween(4, 7);
            for (int sec = 1; sec <= numSections; sec++) {

                // 5. Generate a random section title and create the section
                String sectionTitle = "Module " + sec + ": " + faker.book().title() + " (C" + i + "S" + sec + ")";
                var section = createSectionService.createSection(
                        new CreateSectionCommand(courseId, sectionTitle, sec, instructorId)
                );

                // 6. For each section, create a random number of lessons
                int numLessons = faker.number().numberBetween(2, 5);
                for (int les = 1; les <= numLessons; les++) {

                    // 7. Generate random lesson data and create the lesson
                    String lessonTitle = faker.company().catchPhrase();
                    String videoUrl = "https://www.youtube.com/watch?v=" + faker.internet().uuid().substring(0, 11);
                    String contextText = faker.lorem().sentence();

                    createLessonService.createLesson(new CreateLessonCommand(
                            lessonTitle,
                            videoUrl,
                            contextText,
                            les,
                            courseId,
                            section.id(),
                            instructorId
                    ));
                }
            }
            // 8. After creating all sections and lessons for the course, update the course status to PUBLISHED
            updateCourseStatusService.updateCourseStatus(
                    new UpdateCourseStatusCommand(courseId, CourseStatus.PUBLISHED, instructorId, instructorName)
            );
        }
    }
}
