package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.exception.ConsentChallengeNotValidException;
import ru.spbstu.university.authorizationserver.service.exception.LoginChallengeNotValidException;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class ConsentAcceptService {
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;
    @NonNull
    private final RequestParamsService requestParamsService;

    @NonNull
    public RequestParams accept(@NonNull String challenge, @NonNull String sessionId) {
        final RequestParams requestParams = requestParamsService.get(challenge)
                .orElseThrow(ConsentChallengeNotValidException::new);
        final User user = userService.getBySessionId(sessionId).orElseThrow(UserNotFoundException::new);
        userService.update(user.getSub(), user.getSessionId(), true);

        return requestParams;
    }

    @NonNull
    public ConsentInfo getInfo(@NonNull String challenge, @NonNull String id) {
        final RequestParams requestParams = requestParamsService.get(challenge).orElseThrow(LoginChallengeNotValidException::new);
        final User user = userService.getBySessionId(id).orElseThrow(UserNotFoundException::new);
        final String client_id = requestParams.getMap().getFirst("client_id");
        final Client client = clientService.getByClientId(Objects.requireNonNull(client_id));

        final List<GrantTypesEnum> collect = client.getGrantTypes().stream().map(GrantType::getGrantType).collect(Collectors.toList());
        final List<ResponseTypeEnum> collect1 = client.getResponseTypes().stream().map(ResponseType::getResponseType).collect(Collectors.toList());
        final List<String> collect2 = client.getScopes().stream().map(Scope::getName).collect(Collectors.toList());

        final ConsentAcceptService.ClientInfo clientInfo = new ConsentAcceptService.ClientInfo(client.getId(), collect2, collect, collect1);
        final String redirect_uri = Objects.requireNonNull(requestParams.getMap().getFirst("redirect_uri"));
        final String response_type = Objects.requireNonNull(requestParams.getMap().getFirst("response_type"));

        return new ConsentInfo(user.getSub(), requestParams.getMap().get("scopes"), response_type, redirect_uri, clientInfo);
    }

    @Getter
    @AllArgsConstructor
    public static class ConsentInfo {
        @NonNull
        private final String subject;
        @NonNull
        private final List<String> requestedScopes;
        @NonNull
        private final String requestedResponseType;
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
