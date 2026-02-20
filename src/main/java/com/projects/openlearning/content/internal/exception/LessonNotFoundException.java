package com.projects.openlearning.content.internal.exception;

import com.projects.openlearning.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class LessonNotFoundException extends ResourceNotFoundException {
    public LessonNotFoundException(UUID lessonId) {
        super(String.format("Lesson with id '%s' not found", lessonId));
    }
}
