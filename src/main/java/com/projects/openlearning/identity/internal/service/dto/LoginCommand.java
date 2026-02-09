package com.projects.openlearning.identity.internal.service.dto;

public record LoginCommand(
        String email,
        String password,
        String deviceInfo
) {
}
