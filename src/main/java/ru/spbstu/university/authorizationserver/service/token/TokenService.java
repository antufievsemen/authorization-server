package ru.spbstu.university.authorizationserver.service.token;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.controller.dto.response.IntrospectResponse;
import ru.spbstu.university.authorizationserver.controller.dto.response.TokenResponse;
import ru.spbstu.university.authorizationserver.model.RequestInfo;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.service.RequestInfoService;
import ru.spbstu.university.authorizationserver.service.RevokeTokenService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.encyption.access.AccessTokenEncryptorService;
import ru.spbstu.university.authorizationserver.service.encyption.authcode.exception.AuthCodeIsNotValidException;
import ru.spbstu.university.authorizationserver.service.encyption.openid.OpenidTokenEncryptorService;
import ru.spbstu.university.authorizationserver.service.encyption.pkce.PkceValidatorEncryptorService;
import ru.spbstu.university.authorizationserver.service.encyption.refresh.RefreshTokenEncyptorService;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;
import ru.spbstu.university.authorizationserver.service.flow.exception.GrantTypeNotValidException;
import ru.spbstu.university.authorizationserver.service.token.exception.TokenMissInfoCreationException;

@Service
@AllArgsConstructor
public class TokenService {
    @NonNull
    private final AccessTokenEncryptorService accessTokenEncryptorService;
    @NonNull
    private final RefreshTokenEncyptorService refreshTokenEncyptorService;
    @NonNull
    private final OpenidTokenEncryptorService openidTokenEncryptorService;
    @NonNull
    private final PkceValidatorEncryptorService pkceValidatorEncryptorService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;
    @NonNull
    private final RequestInfoService requestInfoService;
    @NonNull
    private final RevokeTokenService revokeTokenService;

    @NonNull
    public TokenResponse generate(@NonNull String clientId, @NonNull String clientSecret, @Nullable String code,
                                  @NonNull String redirectUri, @NonNull GrantTypesEnum grantType, @Nullable String accessToken,
                                  @Nullable String refreshToken, @Nullable String codeVerifier, @NonNull String sessionId) {
        clientService.getByClientIdAndSecret(clientId, clientSecret);
        if (grantType != GrantTypesEnum.AUTHORIZATION_CODE) {
            throw new GrantTypeNotValidException();
        }

        if (Objects.nonNull(codeVerifier)) {
            pkceValidatorEncryptorService.validate(codeVerifier, sessionId);
        }

        final AccessTokenEncryptorService.TokenInfo tokenInfo = getAccessToken(code, clientId, accessToken);
        final TokenResponse tokenResponse = new TokenResponse(tokenInfo.getToken(), tokenInfo.getTokenType(), tokenInfo.getScopes(), redirectUri);
        if (tokenInfo.getScopes().contains("offline")) {
            tokenResponse.setRefreshToken(getRefreshToken(refreshToken, tokenInfo.getSubject()));
        }

        if (tokenInfo.getScopes().contains("openid") && Objects.nonNull(code)) {
            final User user = userService.get(tokenInfo.getSubject()).orElseThrow(UserNotFoundException::new);
            tokenResponse.setIdToken(openidTokenEncryptorService.create(tokenInfo.getSubject(), tokenInfo.getNonce(),
                    sessionId, accessTokenEncryptorService.getHash(tokenInfo.getToken()), user.getCreatedAt()));
        }

        return tokenResponse;
    }

    @NonNull
    private AccessTokenEncryptorService.TokenInfo getAccessToken(@Nullable String code, @NonNull String clientId,
                                                                 @Nullable String accessToken) {
        if (Objects.nonNull(code)) {
            final RequestInfo requestInfo = requestInfoService.get(code).orElseThrow(AuthCodeIsNotValidException::new);
            requestInfoService.delete(code);
            final List<String> scopes = requestInfo.getRequestParams().getMap().get("granted_scopes");
            return accessTokenEncryptorService.
                    createJwt(Objects.requireNonNull(requestInfo.getSubject()), clientId,
                            requestInfo.getRequestParams().getNonce(),
                            requestInfo.getRequestParams().getMap().get("granted_access_token_audience"),
                            scopes);
        }
        if (Objects.nonNull(accessToken)) {
            return accessTokenEncryptorService.createJwt(accessToken);
        }
        throw new TokenMissInfoCreationException();
    }

    @NonNull
    private String getRefreshToken(@Nullable String refreshToken, @NonNull String subject) {
        if (Objects.nonNull(refreshToken)) {
            refreshTokenEncyptorService.validateRawToken(refreshToken);
        }
        return refreshTokenEncyptorService.create(subject);
    }

    public void revoke(@NonNull String token) {
        final Claims claims = accessTokenEncryptorService.validate(token);
        revokeTokenService.save(token, token, claims.getExpiration());
    }

    @NonNull
    public IntrospectResponse introspect(@NonNull String token, @Nullable List<String> scopes) {
        final Claims claims = accessTokenEncryptorService.validate(token);

        final List<String> tokenScopes = claims.get("scopes", List.class);
        final Date expiration = claims.getExpiration();
        final boolean active = Objects.nonNull(scopes) && tokenScopes.containsAll(scopes) &&
                expiration.before(new Date());

        return new IntrospectResponse(active, claims.get("clientId", String.class), claims.getSubject(),
                tokenScopes, "access_token", claims.getAudience(), claims.getIssuer(), claims.getId(),
                expiration.getTime(), claims.getNotBefore().getTime());
    }
}
