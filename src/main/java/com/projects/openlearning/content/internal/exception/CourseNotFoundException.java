package com.projects.openlearning.content.internal.exception;

import com.projects.openlearning.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class CourseNotFoundException extends ResourceNotFoundException {
    public CourseNotFoundException(UUID courseId) {
        super(String.format("Course with id '%s' not found", courseId));
    }
}
