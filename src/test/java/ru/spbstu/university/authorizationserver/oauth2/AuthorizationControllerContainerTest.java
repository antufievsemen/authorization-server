package ru.spbstu.university.authorizationserver.oauth2;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.spbstu.university.authorizationserver.testcontainers.TestcontainersConfig;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
public class AuthorizationControllerContainerTest {

    @BeforeAll
    public static void setUp() {
        TestcontainersConfig.postgres.start();
        TestcontainersConfig.redis.start();
    }

    @Test
    void init() {
    }

    @AfterAll
    public static void end() {
        TestcontainersConfig.postgres.stop();
        TestcontainersConfig.redis.stop();
    }
}
