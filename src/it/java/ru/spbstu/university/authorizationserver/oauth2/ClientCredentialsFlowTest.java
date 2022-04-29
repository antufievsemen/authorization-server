package ru.spbstu.university.authorizationserver.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.controller.dto.request.IntrospectRequest;
import ru.spbstu.university.authorizationserver.controller.dto.request.TokenRequest;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.testcontainers.TestcontainersConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
public class ClientCredentialsFlowTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientService clientService;

    private Client client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        TestcontainersConfig.postgres.start();
        TestcontainersConfig.redis.start();
    }

    @Test
    void clientCredentialsFlowTest() throws Exception {
        client = clientService.create("my-id", "my-secret",
                List.of(GrantTypeEnum.CLIENT_CREDENTIALS),
                List.of("read:value", "getters"), List.of(ResponseTypeEnum.CODE),
                List.of("http://localhost:8080/v1/clients"));
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        final String uri = factory
                .uriString("/v1/oauth2/token")
                .build().toASCIIString();
        final TokenRequest request = new TokenRequest(client.getClientId(),
                List.of(GrantTypeEnum.CLIENT_CREDENTIALS), client.getClientSecret(),
                null, null, null, null, List.of("read:value", "getters"),
                null);

        final MvcResult result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        final ResultResponse resultResponse = objectMapper.
                readValue(result.getResponse().getContentAsString(), ResultResponse.class);
        getIntrospectToken(resultResponse);
    }

    private void getIntrospectToken(@NonNull ResultResponse resultResponse) throws Exception {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        final String uri = factory
                .uriString("/v1/oauth2/introspect")
                .build().toASCIIString();
        final IntrospectRequest request = new IntrospectRequest(resultResponse.getAccessToken(),
                resultResponse.getScope());
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class ResultResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
        private List<String> scope;
        private String idToken;
    }

    @AfterAll
    public static void end() {
        TestcontainersConfig.postgres.stop();
        TestcontainersConfig.redis.stop();
    }
}
