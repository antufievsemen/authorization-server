package ru.spbstu.university.authorizationserver.service.flow;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.LogoutInfo;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.service.ScopeService;
import ru.spbstu.university.authorizationserver.service.flow.dto.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.ValidRequest;
import ru.spbstu.university.authorizationserver.service.flow.exception.BadRedirectRequestException;
import ru.spbstu.university.authorizationserver.service.flow.exception.BadScopeRequestException;
import ru.spbstu.university.authorizationserver.service.flow.exception.ResponseTypeNotValidException;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class AuthService {
    @NonNull
    private final ClientService clientService;
    @NonNull
    private final ScopeService scopeService;
    @NonNull
    private final AuthManagerService authManagerService;
    @NonNull
    private final LogoutManagerService logoutManagerService;

    @NonNull
    public RedirectResponse authorize(@NonNull String clientId, @NonNull ResponseTypeEnum responseTypeEnum,
                                      @NonNull Optional<String> redirectUri, @NonNull List<String> scopes,
                                      @NonNull String state, @NonNull String nonce, @NonNull Optional<String> codeChallenge,
                                      @NonNull Optional<String> codeChallengeMethod, @NonNull String sessionId,
                                      @NonNull String referer, @NonNull Optional<String> consentVerifier) {
        final Client client = clientService.getByClientId(clientId);
        final ValidRequest validRequest = validateRawRequest(client, responseTypeEnum, redirectUri, scopes, referer);

        return authManagerService.initAuthFlow(sessionId, validRequest, nonce, state, codeChallenge, codeChallengeMethod, consentVerifier);
    }

    @NonNull
    public RedirectResponse logout(@NonNull String sessionId, @NonNull Optional<String> idTokenHint,
                                   @NonNull Optional<String> redirectUri, @NonNull String referer,
                                   @NonNull Optional<String> logoutVerifier) {
        if (logoutVerifier.isPresent()) {
            return logoutManagerService.fullLogout(logoutVerifier.get());
        }
        final LogoutInfo logoutInfo = logoutManagerService.getLogoutInfo(sessionId, idTokenHint);
        final Client client = clientService.getByClientId(logoutInfo.getClientId());
        if (redirectUri.isPresent() && Objects.nonNull(client.getCallback()) &&
                redirectUri.get().equalsIgnoreCase(client.getCallback())) {
            return logoutManagerService.initLogoutFlow(logoutInfo, redirectUri.get());
        }

        return logoutManagerService.initLogoutFlow(logoutInfo, referer);
    }


    @NonNull

    private ValidRequest validateRawRequest(@NonNull Client client, @NonNull ResponseTypeEnum responseTypeEnum, @NonNull Optional<String> redirectUri,
                                            @NonNull List<String> scopes, @NonNull String referer) {
        ValidRequest validRequest = new ValidRequest();
        validRequest.setClientId(client.getClientId());

        validRequest.setResponseTypeEnum(validateResponseType(client.getResponseTypes(), responseTypeEnum));
        setRedirectUri(validRequest, redirectUri, referer, client);

        if (!client.getScopes().containsAll(scopeService.getAllByName(scopes))) {
            throw new BadScopeRequestException();
        }

        validRequest.setGrantTypes(client.getGrantTypes().stream()
                .map(GrantType::getGrantType)
                .collect(toList()));

        return validRequest;
    }

    @NonNull
    private ResponseTypeEnum validateResponseType(@NonNull List<ResponseType> responseTypes, @NonNull ResponseTypeEnum responseTypeEnum) {
        responseTypes.stream()
                .filter(responseType -> responseTypeEnum == responseType.getResponseType())
                .findAny()
                .orElseThrow(ResponseTypeNotValidException::new);

        return responseTypeEnum;
    }

    private void setRedirectUri(@NonNull ValidRequest validRequest, @NonNull Optional<String> redirectUri,
                                @NonNull String referer, @NonNull Client client) {
        redirectUri.ifPresentOrElse(s -> {
            if (Objects.isNull(client.getCallback()) || !client.getCallback().equals(s)) {
                throw new BadRedirectRequestException();
            }
            validRequest.setRedirectUri(s);
        }, () -> validRequest.setRedirectUri(referer));
    }
}
