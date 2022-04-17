package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.authcode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.CompletedParams;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ScopeEnum;
import ru.spbstu.university.authorizationserver.service.CompletedParamsService;
import ru.spbstu.university.authorizationserver.service.PkceParamsService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;
import ru.spbstu.university.authorizationserver.service.auth.exception.GrantTypeNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.codeverifier.CodeVerifierProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.PkceProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception.PkceAuthIncorrectException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.OpenidTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.refresh.RefreshTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.authcode.exception.AuthCodeMissMatchedException;
import ru.spbstu.university.authorizationserver.service.exception.CallbackNotValidException;
import ru.spbstu.university.authorizationserver.service.exception.CodeVerifierExpiredException;

@Component
@AllArgsConstructor
public class AuthCodeTokenResponseBodyGenerator {
    @NonNull
    private final CompletedParamsService completedParamsService;
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final RefreshTokenProvider refreshTokenProvider;
    @NonNull
    private final OpenidTokenProvider openidTokenProvider;
    @NonNull
    private final PkceProvider pkceProvider;
    @NonNull
    private final PkceParamsService pkceParamsService;
    @NonNull
    private final CodeVerifierProvider codeVerifierProvider;

    @NonNull
    public TokenResponseBody generate(@NonNull Client client, @Nullable String code, @Nullable String redirectUri,
                                      @NonNull List<GrantTypeEnum> grantTypes, @NonNull String sessionId,
                                      @Nullable String codeVerifier) {
        final CompletedParams completedParams = validateRequest(code, client, grantTypes, redirectUri, codeVerifier);
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        addAccessToken(map, sessionId, completedParams, client);

        if (grantTypes.contains(GrantTypeEnum.REFRESH_TOKEN)
                && Objects.requireNonNull(completedParams.getScopes()).contains(ScopeEnum.OFFLINE_ACCESS.getName())) {
            addRefreshToken(map, completedParams.getConsentParams().getUser().getId());
        }

        return new TokenResponseBody(map);
    }

    private void addOpenidToken(@NonNull MultiValueMap<String, String> map, @NonNull String sessionId,
                                @NonNull CompletedParams completedParams, @NonNull String token) {
        final String openidToken =
                openidTokenProvider.create(completedParams.getConsentParams().getUser().getId(),
                        completedParams.getConsentParams().getAuthParams().getNonce(),
                        sessionId, accessTokenProvider.getHash(token), completedParams.getUserInfo());

        map.add("id_token", openidToken);
    }

    private void addRefreshToken(@NonNull MultiValueMap<String, String> map,
                                 @NonNull String userId) {
        final String refreshToken = refreshTokenProvider.create(userId);
        map.add("refresh_token", refreshToken);
    }

    private void addAccessToken(@NonNull MultiValueMap<String, String> map, @NonNull String sessionId,
                                @NonNull CompletedParams completedParams, @NonNull Client client) {
        final AccessTokenProvider.TokenInfo jwt =
                accessTokenProvider.createJwt(completedParams.getConsentParams().getUser().getId(),
                        client.getClientId(), sessionId, completedParams.getAud(), completedParams.getScopes());

        map.add("access_token", jwt.getToken());
        map.add("expiresIn", String.valueOf(jwt.getExpiresIn().getTime()));
        map.put("scopes", jwt.getScopes());

        if (completedParams.getScopes().contains(ScopeEnum.OPENID.getName())
                && Objects.nonNull(completedParams.getUserInfo())) {
            addOpenidToken(map, sessionId, completedParams, jwt.getToken());
        }
    }

    @NonNull
    private CompletedParams validateRequest(@Nullable String code, @NonNull Client client,
                                            @NonNull List<GrantTypeEnum> grantTypes, @Nullable String redirectUri,
                                            @Nullable String codeVerifier) {
        if (Objects.isNull(code)) {
            throw new AuthCodeMissMatchedException();
        }
        codeVerifierProvider.validate(code);

        final List<GrantTypeEnum> collectedGrantTypes = client.getGrantTypes().stream()
                .map(GrantType::getType)
                .collect(Collectors.toList());

        if (!collectedGrantTypes.containsAll(grantTypes)) {
            throw new GrantTypeNotValidException();
        }

        final CompletedParams completedParams = completedParamsService.get(code)
                .orElseThrow(CodeVerifierExpiredException::new);

        if (Objects.nonNull(codeVerifier)) {
            pkceProvider.validate(codeVerifier, pkceParamsService.get(code)
                    .orElseThrow(PkceAuthIncorrectException::new));
        }

        if (Objects.isNull(redirectUri)
                || !completedParams.getConsentParams().getAuthParams().getRedirectUri().equals(redirectUri)) {
            throw new CallbackNotValidException();
        }

        return completedParams;
    }
}
