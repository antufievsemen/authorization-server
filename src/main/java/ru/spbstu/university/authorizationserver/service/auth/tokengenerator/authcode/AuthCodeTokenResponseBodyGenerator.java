package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.authcode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ScopeEnum;
import ru.spbstu.university.authorizationserver.model.params.AuthParams;
import ru.spbstu.university.authorizationserver.model.params.ConsentParams;
import ru.spbstu.university.authorizationserver.service.ConsentRequestService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;
import ru.spbstu.university.authorizationserver.service.auth.exception.GrantTypeNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.authcode.exception.AuthCodeIsNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.PkceProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.OpenidTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.refresh.RefreshTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.authcode.exception.AuthCodeMissMatchedException;
import ru.spbstu.university.authorizationserver.service.exception.CallbackNotValidException;

@Component
@AllArgsConstructor
public class AuthCodeTokenResponseBodyGenerator {
    @NonNull
    private final ConsentRequestService consentRequestService;
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final RefreshTokenProvider refreshTokenProvider;
    @NonNull
    private final OpenidTokenProvider openidTokenProvider;
    @NonNull
    private final PkceProvider pkceProvider;

    @NonNull
    public TokenResponseBody generate(@NonNull Client client, @Nullable String code, @Nullable String redirectUri,
                                      @NonNull List<GrantTypeEnum> grantTypes, @NonNull String sessionId,
                                      @NonNull Optional<String> codeVerifier) {
        final ConsentParams consentParams = validateRequest(code, client, grantTypes, redirectUri, codeVerifier);

        final TokenResponseBody tokenResponseBody = new TokenResponseBody(new LinkedMultiValueMap<>());

        addAccessToken(tokenResponseBody, sessionId, consentParams);

        if (grantTypes.contains(GrantTypeEnum.REFRESH_TOKEN)
                && Objects.requireNonNull(consentParams.getScopes()).contains(ScopeEnum.OFFLINE_ACCESS.getName())) {
            addRefreshToken(tokenResponseBody, consentParams);
        }

        return tokenResponseBody;
    }

    private void addOpenidToken(@NonNull TokenResponseBody tokenResponseBody, @NonNull String sessionId,
                                @NonNull ConsentParams consentParams, @NonNull String token) {
        final String openidToken =
                openidTokenProvider.create(Objects.requireNonNull(consentParams.getAuthParams().getSubject()),
                        consentParams.getAuthParams().getNonce(),
                        sessionId, accessTokenProvider.getHash(token), LocalDateTime.now(),
                        Objects.requireNonNull(consentParams.getUserinfo()));

        tokenResponseBody.getAttributes().add("openid_token", openidToken);
    }

    private void addRefreshToken(@NonNull TokenResponseBody tokenResponseBody,
                                 @NonNull ConsentParams consentParams) {
        final String refreshToken =
                refreshTokenProvider.create(Objects.requireNonNull(consentParams.getAuthParams().getSubject()));

        tokenResponseBody.getAttributes().add("refresh_token", refreshToken);
    }

    private void addAccessToken(@NonNull TokenResponseBody tokenResponseBody, @NonNull String sessionId,
                                @NonNull ConsentParams consentParams) {
        final AuthParams authParams = consentParams.getAuthParams();

        final AccessTokenProvider.TokenInfo jwt =
                accessTokenProvider.createJwt(Objects.requireNonNull(authParams.getSubject()),
                        authParams.getAuthClient().getClientId(), sessionId, authParams.getNonce(),
                        consentParams.getAud(), Objects.requireNonNull(consentParams.getScopes()));

        tokenResponseBody.getAttributes().add("access_token", jwt.getToken());
        tokenResponseBody.getAttributes().add("expiresIn", String.valueOf(jwt.getExpiresIn().getTime()));
        tokenResponseBody.getAttributes().put("scopes", jwt.getScopes());

        if (consentParams.getScopes().contains(ScopeEnum.OPENID.getName())
                && Objects.nonNull(consentParams.getUserinfo())) {
            addOpenidToken(tokenResponseBody, sessionId, consentParams, jwt.getToken());
        }
    }

    @NonNull
    private ConsentParams validateRequest(@Nullable String code, @NonNull Client client,
                                          @NonNull List<GrantTypeEnum> grantTypes, @Nullable String redirectUri,
                                          @NonNull Optional<String> codeVerifier) {
        if (Objects.isNull(code)) {
            throw new AuthCodeMissMatchedException();
        }
        final List<GrantTypeEnum> collectedGrantTypes = client.getGrantTypes().stream()
                .map(GrantType::getType)
                .collect(Collectors.toList());
        if (!collectedGrantTypes.containsAll(grantTypes)) {
            throw new GrantTypeNotValidException();
        }
        final ConsentParams consentParams = consentRequestService.get(Objects.requireNonNull(code))
                .orElseThrow(AuthCodeIsNotValidException::new);
        consentRequestService.delete(code);

        if (Objects.isNull(redirectUri) || !consentParams.getAuthParams().getRedirectUri().equals(redirectUri)) {
            throw new CallbackNotValidException();
        }
        codeVerifier.ifPresent(s -> pkceProvider.validate(s,
                Objects.requireNonNull(consentParams.getAuthParams().getPkceParams())));

        return consentParams;
    }
}
