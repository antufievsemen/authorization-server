package ru.spbstu.university.authorizationserver.service.flow;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.service.ScopeService;
import ru.spbstu.university.authorizationserver.service.flow.dto.AuthResponse;
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
    private final FlowManagerService flowManagerService;

    public AuthResponse authorize(@NonNull String clientId, @NonNull ResponseTypeEnum responseTypeEnum,
                                  @NonNull Optional<String> redirectUri, @NonNull List<String> scopes,
                                  @NonNull String state, @NonNull String nonce, @NonNull Optional<String> codeChallenge,
                                  @NonNull Optional<String> codeChallengeMethod, @NonNull String sessionId, @NonNull String referer) {
        final Client client = clientService.getByClientId(clientId);
        final ValidateRequest validateRequest = validateRawRequest(client, responseTypeEnum, redirectUri, scopes, referer);

        return flowManagerService.initFlow(sessionId, validateRequest, nonce, state, codeChallenge, codeChallengeMethod);
    }

    @NonNull
    private ValidateRequest validateRawRequest(@NonNull Client client, @NonNull ResponseTypeEnum responseTypeEnum, @NonNull Optional<String> redirectUri,
                                               @NonNull List<String> scopes, @NonNull String referer) {
        ValidateRequest validateRequest = new ValidateRequest();
        validateRequest.setClientId(client.getClientId());

        validateRequest.setResponseTypeEnum(validateResponseType(client.getResponseTypes(), responseTypeEnum));

        redirectUri.ifPresentOrElse(s -> {
            if (Objects.isNull(client.getRedirectUri()) || !client.getRedirectUri().equals(s)) {
                throw new BadRedirectRequestException();
            }
            validateRequest.setRedirectUri(s);
        }, () -> validateRequest.setRedirectUri(referer));

        if (!client.getScopes().containsAll(scopeService.getAllByName(scopes))) {
            throw new BadScopeRequestException();
        }

        validateRequest.setGrantTypes(client.getGrantTypes().stream()
                .map(GrantType::getGrantType)
                .collect(toList()));

        return validateRequest;
    }

    private ResponseTypeEnum validateResponseType(@NonNull List<ResponseType> responseTypes, @NonNull ResponseTypeEnum responseTypeEnum) {
        responseTypes.stream()
                .filter(responseType -> responseTypeEnum.getName().equals(responseType.getName()))
                .findAny()
                .orElseThrow(ResponseTypeNotValidException::new);

        return responseTypeEnum;
    }

    @Setter
    @Getter
    public static class ValidateRequest {
        @NonNull
        private String clientId;
        @NonNull
        private ResponseTypeEnum responseTypeEnum;
        @NonNull
        private String redirectUri;
        @NonNull
        private List<String> scopes;
        @NonNull
        private List<GrantTypesEnum> grantTypes;
    }
}
