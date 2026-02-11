package com.projects.openlearning.identity.internal.service;

import com.projects.openlearning.common.security.api.TokenIssuerPort;
import com.projects.openlearning.common.security.api.TokenParserPort;
import com.projects.openlearning.identity.internal.exception.SecurityBreachException;
import com.projects.openlearning.identity.internal.model.Session;
import com.projects.openlearning.identity.internal.model.User;
import com.projects.openlearning.identity.internal.repository.SessionRepository;
import com.projects.openlearning.identity.internal.repository.UserRepository;
import com.projects.openlearning.identity.internal.service.dto.RefreshTokenResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenIssuerPort tokenIssuerPort;
    private final TokenParserPort tokenParserPort;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Transactional(noRollbackFor = SecurityBreachException.class)
    public RefreshTokenResult refresh(String refreshToken) {
        // 1. Validate refresh token
        if (tokenParserPort.isRefreshTokenValid(refreshToken)) {
            log.warn("Refresh token is invalid");
            throw new RuntimeException("Invalid refresh token");
        }

        // 2. Find session by finding the token inside
        Session currentSession = sessionRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // 3. Check if session is revoked
        if (currentSession.isRevoked()) {
            log.error("SECURITY BREACH: Attempted reuse of revoked token for user {}",
                    currentSession.getUserId());
            sessionRepository.revokeAllSessions(currentSession.getUserId());
            throw new SecurityBreachException("Token reuse detected. Session revoked for security.");
        }

        // 4. Check expiration & if its valid
        if (currentSession.isExpired() && tokenParserPort.isRefreshTokenValid(refreshToken)) {
            log.warn("Refresh token has expired for user {}", currentSession.getUserId());
            throw new RuntimeException("Refresh token has expired");
        }

        // 5. Get user info from token
        String userEmail = tokenParserPort.getSubjectFromToken(refreshToken);
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 6. Rotate session: create new session and revoke old one
        String newRefreshToken = tokenIssuerPort.generateRefreshToken(
                tokenParserPort.getSubjectFromToken(refreshToken),
                Map.of(
                        "userId", currentUser.getId().toString()
                )
        );
        String newAccessToken = tokenIssuerPort.generateAccessToken(
                tokenParserPort.getSubjectFromToken(refreshToken),
                Map.of(
                        "userId", currentUser.getId().toString(),
                        "userRole", currentUser.getRole().name()
                )
        );
        Instant newExpirationTime = Instant.now().plus(tokenIssuerPort.getRefreshTokenTtl());
        Session newSession = Session.rotate(currentSession, newRefreshToken, newExpirationTime);

        // 7. Persist changes
        sessionRepository.save(currentSession);
        sessionRepository.save(newSession);
        log.info("Refresh token rotated successfully for user {}", currentSession.getUserId());

        return new RefreshTokenResult(newAccessToken, newRefreshToken);
    }
}
