package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.RequestInfo;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.exception.LoginChallengeNotValidException;
import ru.spbstu.university.authorizationserver.service.flow.dto.enums.ResponseRedirect;

@Service
@AllArgsConstructor
public class LoginService {
    @NonNull
    private final RequestInfoService requestInfoService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;

    @NonNull
    public RequestInfo accept(@NonNull String challenge, @NonNull String sub, @NonNull String sessionId) {
        final RequestInfo requestInfo = requestInfoService.get(challenge).orElseThrow(LoginChallengeNotValidException::new);
        userService.get(sub).ifPresentOrElse(user -> userService.update(sub, sessionId),
                () -> {
                    final String clientId = requestInfo.getRequestParams().getClientId();
                    userService.create(sub, clientService.getByClientId(clientId), List.of(), sessionId);
                });

        requestInfoService.delete(challenge);
        requestInfoService.create(UUID.randomUUID().toString(), new RequestInfo(sub, requestInfo.getRequestParams(),
                ResponseRedirect.CONSENT_CHALLENGE));
        return requestInfo;
    }

    @NonNull
    public LoginInfo getInfo(@NonNull String challenge) {
        final RequestInfo requestInfo = requestInfoService.get(challenge).orElseThrow(LoginChallengeNotValidException::new);
        final RequestParams requestParams = requestInfo.getRequestParams();
        final Client client = clientService.getByClientId(requestParams.getClientId());

        final ClientInfo clientInfo = new ClientInfo(client.getId(),
                client.getScopes().stream().map(Scope::getName).collect(Collectors.toList()),
                client.getGrantTypes().stream().map(GrantType::getGrantType).collect(Collectors.toList()),
                client.getResponseTypes().stream().map(ResponseType::getResponseType).collect(Collectors.toList()));
        return new LoginInfo(requestParams.getResponseType(), requestParams.getScopes(),
                requestParams.getRedirectUri(), clientInfo);
    }

    @Getter
    @AllArgsConstructor
    public static class LoginInfo {
        @NonNull
        private final String requestedResponseType;
        @NonNull
        private final List<String> requestedScopes;
        @NonNull
        private final String requestedUrl;
        @NonNull
        private final ClientInfo clientInfo;
    }

    @Getter
    @AllArgsConstructor
    public static class ClientInfo {
        @NonNull
        private final String id;
        @NonNull
        private final List<String> scopes;
        @NonNull
        private final List<GrantTypesEnum> grantTypes;
        @NonNull
        private final List<ResponseTypeEnum> responseTypes;

    }
}
