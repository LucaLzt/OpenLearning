package com.projects.openlearning;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestPropertySource(properties = {
        "application.security.jwt.secret=GenericSecureKeyForIntegrationTesting_MustBeVeryLongWarning__",
        "application.security.jwt.access-expiration-seconds=360",
        "application.security.jwt.refresh-expiration-seconds=864",
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    // 1. We define a static containers to be shared across all test classes.
    // This way we avoid the overhead of starting a new container for each test class.
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");
    static final RedisContainer redis = new RedisContainer("redis:7.4-alpine");
    static final MinIOContainer minio = new MinIOContainer("minio/minio:RELEASE.2025-09-07T16-13-09Z-cpuv1");

    static {
        // 2. Start the containers before any tests run.
        postgres.start();
        redis.start();
        minio.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // 3. Override application properties to point to the containers.
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);

        registry.add("app.aws.s3.endpoint-url", minio::getS3URL);
        registry.add("app.aws.s3.access-key", () -> "admin");
        registry.add("app.aws.s3.secret-key", () -> "password");
    }
}
