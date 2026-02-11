package com.projects.openlearning.identity.internal.service;

import com.projects.openlearning.identity.internal.model.User;
import com.projects.openlearning.identity.internal.repository.UserRepository;
import com.projects.openlearning.identity.internal.service.dto.RegisterCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterCommand command) {
        log.info("Registering user with email: {}", command.email());

        // 1. Create new user entity & encode password
        User user = User.createStudent(
                command.email(),
                command.fullName(),
                passwordEncoder.encode(command.password())
        );

        // 3. Persist User if email is not already taken
        try {
            userRepository.save(user);
            log.info("User with email {} registered successfully", command.email());
        } catch (DataIntegrityViolationException e) {
            log.warn("Registration failed: Email {} is already taken", command.email());
            throw new RuntimeException("Email is already in use");
        }
    }
}
