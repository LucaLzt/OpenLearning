package com.projects.openlearning.common.exception;

public abstract class ForbiddenAccessException extends DomainException {
    protected ForbiddenAccessException(String message) {
        super(message);
    }
}
