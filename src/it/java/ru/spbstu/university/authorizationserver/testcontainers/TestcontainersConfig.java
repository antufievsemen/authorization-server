package ru.spbstu.university.authorizationserver.testcontainers;

import javax.annotation.PostConstruct;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Profile("test")
@Testcontainers
public abstract class TestcontainersConfig {
    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine3.15")
            .withUsername("demouser")
            .withPassword("demopassword")
            .withExposedPorts(5432)
            .withReuse(false);
    @Container
    public static final GenericContainer<?> redis = new GenericContainer<>("redis:alpine3.15")
            .withExposedPorts(6379)
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes");


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),

                    "spring.flyway.url=" + postgres.getJdbcUrl(),
                    "spring.flyway.user=" + postgres.getUsername(),
                    "spring.flyway.password=" + postgres.getPassword(),

                    "spring.redis.port=" + redis.getMappedPort(6379),
                    "spring.redis.host=" + redis.getHost()
            ).applyTo(applicationContext);
        }
    }
}
