package com.projects.openlearning.identity.internal.service.dto;

public record LogoutCommand(
        String accessToken,
        String refreshToken
) {
}
