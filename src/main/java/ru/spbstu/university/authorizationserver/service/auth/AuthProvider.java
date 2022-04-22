package ru.spbstu.university.authorizationserver.service.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.cache.AuthParams;
import ru.spbstu.university.authorizationserver.model.cache.ClientInfo;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ScopeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.auth.exception.RequestParamsNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.codeverifier.CodeVerifierProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.OpenidTokenProvider;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;

@Service
@AllArgsConstructor
public class AuthProvider {
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;
    @NonNull
    private final AuthFlowManager authFlowManager;
    @NonNull
    private final LogoutManagerService logoutManagerService;
    @NonNull
    private final CodeVerifierProvider codeVerifierProvider;
    @NonNull
    private final OpenidTokenProvider openidTokenProvider;

    @NonNull
    public RedirectResponse authorize(@NonNull String clientId, @NonNull List<String> responseTypes,
                                      @NonNull String redirectUri, @NonNull List<String> scopes,
                                      @NonNull String state, @Nullable String nonce,
                                      @NonNull Optional<String> codeChallenge,
                                      @NonNull Optional<String> codeChallengeMethod, @NonNull String sessionId,
                                      @NonNull Optional<String> consentVerifier,
                                      @NonNull Optional<String> loginVerifier) {
        if (consentVerifier.isPresent()) {
            codeVerifierProvider.validate(consentVerifier.get());
            return authFlowManager.endFlow(consentVerifier.get());
        }

        if (loginVerifier.isPresent()) {
            codeVerifierProvider.validate(loginVerifier.get());
            final User user = userService.getByState(state).orElseThrow(UserNotFoundException::new);
            return authFlowManager.consentFlow(user, loginVerifier.get());
        }

        final AuthParams authParams =
                validateAuthParams(clientId, responseTypes, redirectUri, scopes, state, nonce, sessionId);
        final Optional<User> user = userService.getBySessionId(sessionId);

        return user.map(userValue -> authFlowManager.consentFlow(userValue, authParams))
                .orElseGet(() -> authFlowManager.initFlow(authParams, codeChallenge, codeChallengeMethod));
    }

    @NonNull
    private AuthParams validateAuthParams(@NonNull String clientId, @NonNull List<String> responseTypes,
                                          @NonNull String redirectUri, @NonNull List<String> scopes,
                                          @NonNull String state, @Nullable String nonce, @NonNull String sessionId) {
        if (scopes.contains(ScopeEnum.OPENID.getName()) && Objects.isNull(nonce)) {
            throw new RequestParamsNotValidException();
        }

        final List<ResponseTypeEnum> responseTypeEnums = responseTypes.stream()
                .map(s -> ResponseTypeEnum.valueOf(s.toUpperCase()))
                .collect(Collectors.toList());
        final List<GrantTypeEnum> grantTypes =
                getGrantType(Objects.requireNonNull(responseTypeEnums), Objects.requireNonNull(scopes));

        final ClientInfo clientInfo = clientService.validate(Objects.requireNonNull(clientId), responseTypeEnums,
                grantTypes, scopes, Objects.requireNonNull(redirectUri));

        return new AuthParams(clientInfo, responseTypeEnums, grantTypes, scopes, redirectUri, state, sessionId, nonce);
    }

    @NonNull
    private List<GrantTypeEnum> getGrantType(@NonNull List<ResponseTypeEnum> responseTypes,
                                             @NonNull List<String> scopes) {
        if (responseTypes.contains(ResponseTypeEnum.TOKEN)
                && !responseTypes.contains(ResponseTypeEnum.CODE)) {
            return getImplicitGrantType();
        }
        if (responseTypes.contains(ResponseTypeEnum.CODE) &&
                scopes.contains(ScopeEnum.OFFLINE_ACCESS.getName())) {
            return getAuthCodeAndOfflineGrantTypes();
        }
        if (responseTypes.contains(ResponseTypeEnum.CODE)
                && !responseTypes.contains(ResponseTypeEnum.TOKEN)) {
            return getAuthCodeGrantTypes();
        }
        throw new RequestParamsNotValidException();
    }

    @NonNull
    private List<GrantTypeEnum> getAuthCodeGrantTypes() {
        return new ArrayList<>(List.of(GrantTypeEnum.AUTHORIZATION_CODE));
    }

    @NonNull
    private List<GrantTypeEnum> getAuthCodeAndOfflineGrantTypes() {
        return new ArrayList<>(List.of(GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.REFRESH_TOKEN));
    }

    @NonNull
    private List<GrantTypeEnum> getImplicitGrantType() {
        return new ArrayList<>(List.of(GrantTypeEnum.IMPLICIT));
    }

    @NonNull
    public RedirectResponse logout(@NonNull String sessionId, @NonNull Optional<String> redirectUri,
                                   @NonNull Optional<String> logoutVerifier, @NonNull String idTokenHint,
                                   @NonNull Optional<String> state) {
        if (logoutVerifier.isPresent()) {
            codeVerifierProvider.validate(logoutVerifier.get());
            return logoutManagerService.logout(redirectUri, logoutVerifier.get(), state);
        }

        final Optional<User> user = userService.getBySessionId(sessionId);
        return user.map(value -> logoutManagerService.logout(value, idTokenHint, state, redirectUri))
                .orElseGet(() -> logoutManagerService.logout(redirectUri, state));
    }
}
