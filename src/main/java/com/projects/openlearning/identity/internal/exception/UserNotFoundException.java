package com.projects.openlearning.identity.internal.exception;

import com.projects.openlearning.common.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String email) {
        super(String.format("User with email '%s' not found", email));
    }
}
