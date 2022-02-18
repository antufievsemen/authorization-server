package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.refresh.RefreshTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.refresh.exception.RefreshTokenExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.refresh.exception.RefreshTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken.exception.RefreshAccessTokenException;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken.exception.RefreshTokenValidationException;

@Component
@AllArgsConstructor
public class RefreshAccessTokenResponseBodyGenerator {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final RefreshTokenProvider refreshTokenProvider;

    @NonNull
    public TokenResponseBody generate(@Nullable String accessToken,
                                      @Nullable String refreshToken) {
        validate(accessToken, refreshToken);

        final TokenResponseBody tokenResponseBody = new TokenResponseBody(new LinkedMultiValueMap<>());
        final AccessTokenProvider.TokenInfo jwt = accessTokenProvider.createJwt(Objects.requireNonNull(accessToken));
        final String newRefreshToken = refreshTokenProvider.update(jwt.getSubject());

        tokenResponseBody.getAttributes().add("access_token", jwt.getToken());
        tokenResponseBody.getAttributes().add("expiresIn", String.valueOf(jwt.getExpiresIn().getTime()));
        tokenResponseBody.getAttributes().put("scopes", jwt.getScopes());
        tokenResponseBody.getAttributes().add("refresh_token", newRefreshToken);

        return tokenResponseBody;
    }

    private void validate(@Nullable String accessToken, @Nullable String refreshToken) {
        if (Objects.isNull(accessToken) || Objects.isNull(refreshToken)) {
            throw new RefreshAccessTokenException();
        }
        accessTokenProvider.validate(accessToken);

        try {
            refreshTokenProvider.validateRawToken(refreshToken);
        } catch (RefreshTokenExpiredException | RefreshTokenNotValidException e) {
            throw new RefreshTokenValidationException();
        }
    }
}
