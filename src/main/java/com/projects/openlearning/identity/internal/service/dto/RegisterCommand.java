package com.projects.openlearning.identity.internal.service.dto;

public record RegisterCommand(
        String fullName,
        String email,
        String password
) {
}
