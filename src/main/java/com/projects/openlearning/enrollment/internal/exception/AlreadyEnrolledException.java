package com.projects.openlearning.enrollment.internal.exception;

import com.projects.openlearning.common.exception.ConflictException;

import java.util.UUID;

public class AlreadyEnrolledException extends ConflictException {
    public AlreadyEnrolledException(UUID courseId) {
        super(String.format("Already enrolled in course with id '%s'", courseId));
    }
}
