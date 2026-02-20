package com.projects.openlearning.common.exception;

public abstract class ResourceNotFoundException extends DomainException {
    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
