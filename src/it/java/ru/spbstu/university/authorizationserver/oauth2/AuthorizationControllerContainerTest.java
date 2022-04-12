package ru.spbstu.university.authorizationserver.oauth2;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ScopeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.testcontainers.TestcontainersConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
public class AuthorizationControllerContainerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientService clientService;

    private Client client;

    @BeforeAll
    public static void setUp() {
        TestcontainersConfig.postgres.start();
        TestcontainersConfig.redis.start();
    }

    @Test
    void getAuthorizationCodeTest() throws Exception {
        client = clientService.create("my-id", "my-secret",
                List.of(GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.REFRESH_TOKEN),
                List.of(ScopeEnum.OFFLINE_ACCESS.name()), List.of(ResponseTypeEnum.CODE),
                List.of("http://localhost:8080/v1/clients"));

        final String uri = new DefaultUriBuilderFactory("http://localhost:8080")
                .uriString("/v1/oauth2/auth")
                .queryParam("state", "state12")
                .queryParam("client_id", "my-id")
                .queryParam("redirect_uri", "http://localhost:8080/v1/clients")
                .queryParam("response_type", "code")
                .queryParam("scope", "offline_access")
                .build().toASCIIString();
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @AfterAll
    public static void end() {
        TestcontainersConfig.postgres.stop();
        TestcontainersConfig.redis.stop();
    }
}
