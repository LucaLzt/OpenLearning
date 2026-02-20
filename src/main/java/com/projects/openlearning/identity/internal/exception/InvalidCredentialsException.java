package com.projects.openlearning.identity.internal.exception;

import com.projects.openlearning.common.exception.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super("The email or password provided is incorrect");
    }
}
