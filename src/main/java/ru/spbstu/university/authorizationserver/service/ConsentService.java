package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.RequestInfo;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.exception.ConsentChallengeNotValidException;
import ru.spbstu.university.authorizationserver.service.exception.ScopeNotValidException;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;
import ru.spbstu.university.authorizationserver.service.flow.dto.enums.ResponseRedirect;

@Service
@Transactional
@AllArgsConstructor
public class ConsentService {
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;
    @NonNull
    private final RequestInfoService requestInfoService;

    @NonNull
    public MultiValueMap<String, String> accept(@NonNull String challenge, @NonNull List<String> grantedScopes, @NonNull List<String> grantedAccessTokenAudience) {
        final RequestInfo requestInfo = requestInfoService.get(challenge).orElseThrow(ConsentChallengeNotValidException::new);
        requestInfoService.delete(challenge);

        if (!requestInfo.getRequestParams().getScopes().containsAll(grantedScopes)) {
            throw new ScopeNotValidException();
        }

        final MultiValueMap<String, String> map = requestInfo.getRequestParams().getMap();
        userService.get(Objects.requireNonNull(requestInfo.getSubject())).orElseThrow(UserNotFoundException::new);
        requestInfo.getRequestParams().getMap().put("granted_scopes", grantedScopes);
        requestInfo.getRequestParams().getMap().put("granted_access_token_audience", grantedAccessTokenAudience);

        final String consentVerifier = UUID.randomUUID().toString();
        requestInfoService.create(consentVerifier, new RequestInfo(requestInfo.getSubject(),
                requestInfo.getRequestParams(), ResponseRedirect.CONSENT_VERIFIER));
        map.add("consent_verifier", consentVerifier);

        return map;
    }

    @NonNull
    public ConsentInfo getInfo(@NonNull String challenge, @NonNull String id) {
        final RequestInfo requestInfo = requestInfoService.get(challenge).orElseThrow(ConsentChallengeNotValidException::new);
        final User user = userService.getBySessionId(id).orElseThrow(UserNotFoundException::new);
        final Client client = clientService.getByClientId(requestInfo.getRequestParams().getClientId());

        final ConsentService.ClientInfo clientInfo = new ConsentService.ClientInfo(client.getId(),
                client.getScopes().stream().map(Scope::getName).collect(Collectors.toList()),
                client.getGrantTypes().stream().map(GrantType::getGrantType).collect(Collectors.toList()),
                client.getResponseTypes().stream().map(ResponseType::getResponseType).collect(Collectors.toList()));

        return new ConsentInfo(user.getSub(), requestInfo.getRequestParams().getScopes(),
                requestInfo.getRequestParams().getResponseType(), requestInfo.getRequestParams().getRedirectUri(), clientInfo);
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
