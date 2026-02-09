package com.projects.openlearning.identity.internal.service.dto;

public record RefreshTokenResult(
        String accessToken,
        String refreshToken
) {
}
