package com.projects.openlearning.content.internal.exception;

import com.projects.openlearning.common.exception.ForbiddenAccessException;

import java.util.UUID;

public class CourseOwnershipException extends ForbiddenAccessException {
    public CourseOwnershipException(UUID courseId) {
        super(String.format("You do not have permission to perform this action on the course (ID: '%s')", courseId));
    }
}
