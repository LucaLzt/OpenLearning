package com.projects.openlearning.content.internal.exception;

import com.projects.openlearning.common.exception.ForbiddenAccessException;

import java.util.UUID;

public class StudentEnrollmentException extends ForbiddenAccessException {
    public StudentEnrollmentException(UUID courseId) {
        super(String.format("Student does not have access to the course with id '%s'", courseId));
    }
}
