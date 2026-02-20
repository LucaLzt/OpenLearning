package com.projects.openlearning.identity.internal.exception;

import com.projects.openlearning.common.exception.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String email) {
        super(String.format("User with email '%s' already exists", email));
    }
}
