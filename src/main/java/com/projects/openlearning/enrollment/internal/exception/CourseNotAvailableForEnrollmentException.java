package com.projects.openlearning.enrollment.internal.exception;

import com.projects.openlearning.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class CourseNotAvailableForEnrollmentException extends ResourceNotFoundException {
    public CourseNotAvailableForEnrollmentException(UUID courseId) {
        super(String.format("Course with id %s is not available for enrollment", courseId));
    }
}
