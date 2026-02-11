package com.projects.openlearning.common.exception;

public abstract class UnauthorizedException extends DomainException {
    protected UnauthorizedException(String message) {
        super(message);
    }
}
