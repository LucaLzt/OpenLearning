package com.projects.openlearning.common.security.api;

import java.time.Duration;
import java.util.Map;

public interface TokenIssuerPort {

    String generateAccessToken(String subject, Map<String, Object> claims);

    String generateRefreshToken(String subject, Map<String, Object> claims);

    Duration getRefreshTokenTtl();

    void revokeToken(String token);
}
