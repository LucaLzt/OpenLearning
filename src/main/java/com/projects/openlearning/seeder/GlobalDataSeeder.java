package com.projects.openlearning.seeder;

import com.projects.openlearning.content.api.ContentSeederApi;
import com.projects.openlearning.identity.api.IdentitySeederApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("local")
@RequiredArgsConstructor
public class GlobalDataSeeder {

    private final IdentitySeederApi identitySeederApi;
    private final ContentSeederApi contentSeederApi;

    @Bean
    CommandLineRunner init() {
        return args -> {
            log.info("Initializing global data seeding...");

            // 1. Delegate to Identity Module Seeder
            var users = identitySeederApi.seedUsers();

            // 2. Delegate to Content Module Seeder using the seeded instructor's ID and name
            contentSeederApi.seedRandomCourses(users.instructorId(), users.instructorName(), 5);

            log.info("Global data seeding completed successfully.");
        };
    }
}
