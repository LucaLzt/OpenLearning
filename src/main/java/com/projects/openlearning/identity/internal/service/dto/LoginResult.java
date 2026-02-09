package com.projects.openlearning.identity.internal.service.dto;

public record LoginResult(
        String accessToken,
        String refreshToken
) {
}
