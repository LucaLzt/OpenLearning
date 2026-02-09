package com.projects.openlearning.identity.internal.seed;

import com.projects.openlearning.identity.internal.model.User;
import com.projects.openlearning.identity.internal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@Profile("local") // Only run this seeding in the local development environment
public class DataSeederConfig {

    private final PasswordEncoder passwordEncoder;

    public DataSeederConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Seed initial users for development environment
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // 1. Create sample users with encoded passwords
                var user1 = User.createStudent("student@example.com", "John Doe", passwordEncoder.encode("studentPassword"));
                var user2 = User.createInstructor("instructor@example.com", "Albert Doe", passwordEncoder.encode("instructorPassword"));

                // 2. Save users to the database
                userRepository.save(user1);
                userRepository.save(user2);
                log.info("Seeded initial users: {} - Role: {} | {} - Role: {}", user1.getEmail(), user1.getRole(), user2.getEmail(), user2.getRole());
            }
        };
    }
}
