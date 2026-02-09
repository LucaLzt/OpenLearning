package com.projects.openlearning.identity.internal.web.dto;

public record LoginRequest(
        String email,
        String password
) {
}
