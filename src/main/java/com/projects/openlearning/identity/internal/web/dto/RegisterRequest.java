package com.projects.openlearning.identity.internal.web.dto;

public record RegisterRequest(
        String fullName,
        String email,
        String password
) {
}
