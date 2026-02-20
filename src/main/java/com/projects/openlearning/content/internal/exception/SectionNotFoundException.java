package com.projects.openlearning.content.internal.exception;

import com.projects.openlearning.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class SectionNotFoundException extends ResourceNotFoundException {
    public SectionNotFoundException(UUID sectionId) {
        super(String.format("Section with id '%s' not found", sectionId));
    }
}
