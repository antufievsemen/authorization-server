package ru.spbstu.university.authorizationserver.oauth2;

import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.spbstu.university.authorizationserver.controller.dto.request.TokenRequest;
import ru.spbstu.university.authorizationserver.controller.flow.dto.request.ConsentRequest;
import ru.spbstu.university.authorizationserver.controller.flow.dto.request.LoginRequest;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ScopeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.testcontainers.TestcontainersConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        TestcontainersConfig.postgres.start();
        TestcontainersConfig.redis.start();
    }

    @Test
    void getAuthorizationCodeTest() throws Exception {
        client = clientService.create("my-id", "my-secret",
                List.of(GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.REFRESH_TOKEN),
                List.of(ScopeEnum.OFFLINE_ACCESS.getName()), List.of(ResponseTypeEnum.CODE),
                List.of("http://localhost:8080/v1/clients"));

        final String uri = new DefaultUriBuilderFactory("http://localhost:8080")
                .uriString("/v1/oauth2/auth")
                .queryParam("state", "state12")
                .queryParam("client_id", "my-id")
                .queryParam("redirect_uri", "http://localhost:8080/v1/clients")
                .queryParam("response_type", "code")
                .queryParam("scope", "offline_access")
                .build().toASCIIString();
        final MvcResult mvcResult = mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isMovedPermanently())
                .andReturn();
        final Object loginVerifier = mvcResult.getModelAndView().getModel().get("login_verifier");
        getLoginInfo(loginVerifier);
        final String loginAcceptUri = getLoginAccept(loginVerifier);

        final Object consentVerifier = getConsentFlow(loginAcceptUri);
        getConsentInfo(consentVerifier);
        final String consentAcceptUri = getConsentAccept(consentVerifier);
        final MvcResult result = getAuthCode(consentAcceptUri);
        final MvcResult tokenResult = getToken(result.getModelAndView().getModel().get("code"));
        System.out.println("awdaw");
    }

    public void getLoginInfo(@NonNull Object loginVerifier) throws Exception {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        final String uri = factory
                .uriString("/v1/oauth2/auth/requests/login")
                .queryParam("login_verifier", loginVerifier)
                .build().toASCIIString();
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @NonNull
    public String getLoginAccept(@NonNull Object loginVerifier) throws Exception {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        final String uri = factory
                .uriString("/v1/oauth2/auth/requests/login/accept")
                .queryParam("login_verifier", loginVerifier)
                .build().toASCIIString();
        final LoginRequest request = new LoginRequest("userOne");
        final MvcResult mvcResult = mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

    @NonNull
    public Object getConsentFlow(@NonNull String uri) throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(URI.create(uri)))
                .andDo(print())
                .andExpect(status().isMovedPermanently())
                .andReturn();

        return mvcResult.getModelAndView().getModel().get("consent_verifier");
    }

    public void getConsentInfo(@NonNull Object consentVerifier) throws Exception {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        final String uri = factory
                .uriString("/v1/oauth2/auth/requests/consent")
                .queryParam("consent_verifier", consentVerifier)
                .build().toASCIIString();
        final MvcResult mvcResult = mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @NonNull
    public String getConsentAccept(@NonNull Object consentVerifier) throws Exception {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        final String uri = factory
                .uriString("/v1/oauth2/auth/requests/consent/accept")
                .queryParam("consent_verifier", consentVerifier)
                .build().toASCIIString();
        final ConsentRequest request = new ConsentRequest(List.of(ScopeEnum.OFFLINE_ACCESS.getName()),
                List.of("authorization-server"), Map.of());
        final MvcResult mvcResult = mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

    @NonNull
    public MvcResult getAuthCode(@NonNull String uri) throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(URI.create(uri)))
                .andDo(print())
                .andExpect(status().isMovedPermanently())
                .andReturn();

        return mvcResult;
    }

    @NonNull
    public MvcResult getToken(@NonNull Object code) throws Exception {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        final String uri = factory
                .uriString("/v1/oauth2/token")
                .build().toASCIIString();
        final TokenRequest request = new TokenRequest(client.getClientId(),
                List.of(GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.REFRESH_TOKEN), client.getClientSecret(),
                (String) code, null, null, client.getCallbacks().get(0).getUrl(), null, null);

        return mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @AfterAll
    public static void end() {
        TestcontainersConfig.postgres.stop();
        TestcontainersConfig.redis.stop();
    }
}
