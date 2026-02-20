package com.projects.openlearning.identity.internal.seed;

import com.projects.openlearning.identity.api.IdentitySeederApi;
import com.projects.openlearning.identity.internal.model.User;
import com.projects.openlearning.identity.internal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdentitySeederImpl implements IdentitySeederApi {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SeededUsers seedUsers() {

        // 1. Check if users already exist to avoid duplicate seeding
        if (userRepository.count() > 0) {
            log.info("Identity module already seeded.");
            var existingInstructor = userRepository.findAll().stream()
                    .filter(u -> "INSTRUCTOR".equals(u.getRole().name())).findFirst().orElseThrow();
            return new SeededUsers(UUID.randomUUID(), existingInstructor.getId(), existingInstructor.getFullName());
        }

        // 2. Initialize Faker for generating random data
        Faker faker = new Faker();

        // 3. Create a test student and instructor with predefined credentials
        var student = User.createStudent(
                "student@openlearning.com",
                faker.name().fullName(), // Ej: "Dr. Johnathon Doe"
                passwordEncoder.encode("123456")
        );

        var instructor = User.createInstructor(
                "instructor@openlearning.com",
                faker.name().fullName(), // Ej: "Mrs. Jane Smith"
                passwordEncoder.encode("123456")
        );

        // 4. Save the users to the repository
        userRepository.save(student);
        userRepository.save(instructor);
        log.info("Identity Seeded: Instructor ID: {}, Student ID: {}", instructor.getId(), student.getId());

        // 5. Return the IDs and instructor name for use in seeding other modules
        return new SeededUsers(student.getId(), instructor.getId(), instructor.getFullName());
    }
}
