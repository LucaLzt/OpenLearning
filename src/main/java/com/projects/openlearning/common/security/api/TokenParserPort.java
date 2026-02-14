package com.projects.openlearning.common.security.api;

import org.springframework.security.core.Authentication;

import java.time.Instant;

public interface TokenParserPort {

    boolean isAccessTokenValid(String token);

    boolean isRefreshTokenValid(String token);

    String getSubjectFromToken(String token);

    String getUserIdFromToken(String token);

    String getJtiFromToken(String token);

    Instant getExpirationFromToken(String token);

    Authentication getAuthentication(String token);

}
