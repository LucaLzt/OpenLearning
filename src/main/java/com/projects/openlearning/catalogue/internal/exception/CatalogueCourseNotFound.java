package com.projects.openlearning.catalogue.internal.exception;

import com.projects.openlearning.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class CatalogueCourseNotFound extends ResourceNotFoundException {
    public CatalogueCourseNotFound(UUID courseId) {
        super(String.format("Course with id '%s' not found in catalogue", courseId));
    }
}
