package com.projects.openlearning.content.internal.exception;

import com.projects.openlearning.common.exception.ConflictException;

public class InvalidCourseStateException extends ConflictException {
    public InvalidCourseStateException(String message) {
        super(message);
    }
}
