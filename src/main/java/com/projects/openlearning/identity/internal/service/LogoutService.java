package com.projects.openlearning.identity.internal.service;

import com.projects.openlearning.common.security.api.TokenIssuerPort;
import com.projects.openlearning.identity.internal.model.Session;
import com.projects.openlearning.identity.internal.repository.SessionRepository;
import com.projects.openlearning.identity.internal.service.dto.LogoutCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService {

    private final TokenIssuerPort tokenIssuerPort;
    private final SessionRepository sessionRepository;

    @Transactional
    public void logout(LogoutCommand request) {
        log.info("Logout request received for access token: {}", request.accessToken());

        // 1. Calculate the TTL for the blacklist entry based on token expiration
        tokenIssuerPort.revokeToken(request.accessToken());

        // 3. Revoke the refresh token in the session repository
        Session storedSession = sessionRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> {
                    log.warn("Logout warning: Session not found for token");
                    return new RuntimeException("Sesión no encontrada");
                });
        storedSession.revoke();
        sessionRepository.save(storedSession);

        log.info("Logout successful for access token: {}", request.accessToken());
    }
}
