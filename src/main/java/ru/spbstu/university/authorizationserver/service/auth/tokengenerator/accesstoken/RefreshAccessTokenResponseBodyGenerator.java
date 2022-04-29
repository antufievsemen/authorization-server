package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken;

import io.jsonwebtoken.Claims;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.model.enums.TokenType;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.IntrospectBody;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception.AccessTokenIsExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.refresh.RefreshTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken.exception.RefreshAccessTokenException;

@Component
@AllArgsConstructor
public class RefreshAccessTokenResponseBodyGenerator {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final RefreshTokenProvider refreshTokenProvider;

    @NonNull
    public TokenResponseBody generate(@Nullable String accessToken, @Nullable String refreshToken,
                                      @NonNull String clientId) {
        final Claims claims = validate(accessToken, refreshToken);

        final AccessTokenProvider.TokenInfo jwt = accessTokenProvider.createJwt(claims, clientId);
        final String newRefreshToken = refreshTokenProvider.update(jwt.getSubject());

        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("access_token", jwt.getToken());
        map.add("token_type", TokenType.JWT.getType());
        map.add("expiresIn", String.valueOf(jwt.getExpiresIn().getTime()));
        map.put("scope", jwt.getScopes());
        map.add("refresh_token", newRefreshToken);

        return new TokenResponseBody(map);
    }

    @NonNull
    private Claims validate(@Nullable String accessToken, @Nullable String refreshToken) {
        if (Objects.isNull(accessToken) || Objects.isNull(refreshToken)) {
            throw new RefreshAccessTokenException();
        }

        refreshTokenProvider.validateRawToken(refreshToken);
        final IntrospectBody introspect = accessTokenProvider.introspect(accessToken);
        if (!introspect.isActive()) {
            throw new AccessTokenIsExpiredException();
        }
        return introspect.getClaims();
    }
}
