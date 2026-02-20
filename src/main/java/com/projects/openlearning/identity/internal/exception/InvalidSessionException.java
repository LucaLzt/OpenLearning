package com.projects.openlearning.identity.internal.exception;

import com.projects.openlearning.common.exception.UnauthorizedException;

public class InvalidSessionException extends UnauthorizedException {
    public InvalidSessionException(String message) {
        super(message);
    }
}
