package com.projects.openlearning.identity.internal.exception;

import com.projects.openlearning.common.exception.UnauthorizedException;

public class SecurityBreachException extends UnauthorizedException {
    public SecurityBreachException(String message) {
        super(message);
    }
}
