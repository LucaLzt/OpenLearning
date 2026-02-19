package com.projects.openlearning.content.internal.service.model;

import com.projects.openlearning.content.internal.model.Course;
import com.projects.openlearning.content.internal.model.Lesson;
import com.projects.openlearning.content.internal.model.Section;

import java.util.List;
import java.util.UUID;

public record StudentCourseView(
        UUID id,
        String title,
        List<StudentSectionView> sections
) {

    public record StudentSectionView(
            UUID id,
            String title,
            Integer orderIndex,
            List<StudentLessonView> lessons
    ) {
    }

    public record StudentLessonView(
            UUID id,
            String title,
            Integer orderIndex
    ) {
    }

    public static StudentCourseView fromEntity(Course course) {
        if (course == null) {
            return null;
        }
        List<StudentSectionView> sectionViews = course.getSections() != null
                ? course.getSections().stream()
                .map(StudentCourseView::mapSection)
                .toList()
                : List.of();

        return new StudentCourseView(
                course.getId(),
                course.getTitle(),
                sectionViews
        );
    }

    private static StudentSectionView mapSection(Section section) {
        List<StudentLessonView> lessonViews = section.getLessons() != null
                ? section.getLessons().stream()
                .map(StudentCourseView::mapLesson)
                .toList()
                : List.of();

        return new StudentSectionView(
                section.getId(),
                section.getTitle(),
                section.getOrderIndex(),
                lessonViews
        );
    }

    private static StudentLessonView mapLesson(Lesson lesson) {
        return new StudentLessonView(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getOrderIndex()
        );
    }
}
