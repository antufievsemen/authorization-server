package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.exception.LoginChallengeNotValidException;

@Service
@AllArgsConstructor
public class LoginAcceptService {
    @NonNull
    private final RequestParamsService requestParamsService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;

    @NonNull
    public RequestParams accept(@NonNull String challenge, @NonNull String sub, @NonNull String sessionId) {
        final RequestParams requestParams = requestParamsService.get(challenge).orElseThrow(LoginChallengeNotValidException::new);
        userService.get(sub).ifPresentOrElse(user -> userService.update(sub, sessionId, false),
                () -> {
                    final MultiValueMap<String, String> map = requestParams.getMap();
                    final String clientId = map.get("client_id").stream().findFirst().orElseThrow();
                    userService.create(sub, clientService.getByClientId(clientId), List.of(), sessionId);
                });

        return requestParams;
    }

    @NonNull
    public LoginInfo getInfo(@NonNull String challenge) {
        final RequestParams requestParams = requestParamsService.get(challenge).orElseThrow(LoginChallengeNotValidException::new);
        final String client_id = requestParams.getMap().getFirst("client_id");
        final Client client = clientService.getByClientId(Objects.requireNonNull(client_id));

        final List<GrantTypesEnum> collect = client.getGrantTypes().stream().map(GrantType::getGrantType).collect(Collectors.toList());
        final List<ResponseTypeEnum> collect1 = client.getResponseTypes().stream().map(ResponseType::getResponseType).collect(Collectors.toList());
        final List<String> collect2 = client.getScopes().stream().map(Scope::getName).collect(Collectors.toList());

        final ClientInfo clientInfo = new ClientInfo(client.getId(), collect2, collect, collect1);
        final String redirect_uri = Objects.requireNonNull(requestParams.getMap().getFirst("redirect_uri"));
        final String response_type = Objects.requireNonNull(requestParams.getMap().getFirst("response_type"));

        return new LoginInfo(response_type, requestParams.getMap().get("scopes"), redirect_uri, clientInfo);
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
