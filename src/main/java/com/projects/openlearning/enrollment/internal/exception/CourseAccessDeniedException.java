package com.projects.openlearning.enrollment.internal.exception;

import com.projects.openlearning.common.exception.ForbiddenAccessException;

import java.util.UUID;

public class CourseAccessDeniedException extends ForbiddenAccessException {
    public CourseAccessDeniedException(UUID courseId) {
        super(String.format("Access to course with id '%s' is denied", courseId));
    }
}
