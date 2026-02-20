package com.projects.openlearning.identity.internal.service;

import com.projects.openlearning.common.security.api.TokenParserPort;
import com.projects.openlearning.identity.internal.exception.UserNotFoundException;
import com.projects.openlearning.identity.internal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeRoleService {

    private final UserRepository userRepository;
    private final TokenParserPort tokenParserPort;

    public void changeRole(String refreshToken) {
        log.info("Changing roles for refresh token: {}", refreshToken);

        // 1. Extract user ID from token & transform to UUID
        String userIdStr = tokenParserPort.getUserIdFromToken(refreshToken);
        UUID userId = UUID.fromString(userIdStr);

        // 2. Find user by ID
        var user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    String userEmail = tokenParserPort.getSubjectFromToken(refreshToken);
                    log.warn("User with email {} not found", userEmail);
                    return new UserNotFoundException(userEmail);
                });

        // 3. Change role
        user.changeRole();

        // 4. Persist changes in db
        userRepository.save(user);

        log.info("Role changed successfully for user with ID {}", userId);
    }
}