package com.projects.openlearning.identity.internal.service;

import com.projects.openlearning.common.security.api.TokenIssuerPort;
import com.projects.openlearning.identity.internal.exception.InvalidCredentialsException;
import com.projects.openlearning.identity.internal.exception.UserNotFoundException;
import com.projects.openlearning.identity.internal.model.Session;
import com.projects.openlearning.identity.internal.model.User;
import com.projects.openlearning.identity.internal.repository.SessionRepository;
import com.projects.openlearning.identity.internal.repository.UserRepository;
import com.projects.openlearning.identity.internal.service.dto.LoginCommand;
import com.projects.openlearning.identity.internal.service.dto.LoginResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenIssuerPort tokenIssuerPort;

    @Transactional
    public LoginResult login(LoginCommand request) {
        log.info("Login request for user: {}", request.email());

        // 1. Validate user existence
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found for email {}", request.email());
                    return new UserNotFoundException(request.email());
                });

        // 2. Validate password
        boolean matches = passwordEncoder.matches(request.password(), user.getPasswordHash());
        if (!matches) {
            log.warn("Login failed: Invalid password for user {}", request.email());
            throw new InvalidCredentialsException();
        }

        // 3. Generate JWT token
        String accessToken = tokenIssuerPort.generateAccessToken(
                user.getEmail(),
                Map.of(
                        "userId", user.getId().toString(),
                        "userRole", user.getRole().name()
                )
        );
        String refreshToken = tokenIssuerPort.generateRefreshToken(
                user.getEmail(),
                Map.of(
                        "userId", user.getId().toString()
                )
        );

        // 4. Create and persist session
        Session session = Session.createFirstSession(
                user.getId(),
                refreshToken,
                request.deviceInfo(),
                Instant.now().plus(tokenIssuerPort.getRefreshTokenTtl())
        );

        // 5. Save session
        sessionRepository.save(session);
        log.info("User {} logged in successfully.", request.email());
        return new LoginResult(accessToken, refreshToken);
    }
}
