package ru.spbstu.university.authorizationserver.service.auth;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.service.RevokeTokenService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.IntrospectBody;
import ru.spbstu.university.authorizationserver.service.auth.exception.TokenRevokeException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;

@Service
@AllArgsConstructor
public class IntrospectManager {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final RevokeTokenService revokeTokenService;

    @NonNull
    public IntrospectBody introspect(@NonNull String token, @Nullable List<String> scopes) {
        final Optional<String> revokedToken = revokeTokenService.get(token);
        if (revokedToken.isPresent()) {
            throw new TokenRevokeException();
        }

        final IntrospectBody introspect = accessTokenProvider.introspect(token);
        final Claims claims = introspect.getClaims();
        final List<String> availableScopes = claims.get("scope", List.class);

        if (Objects.nonNull(scopes)) {
            if (availableScopes.containsAll(scopes)) {
                return new IntrospectBody(false, claims);
            }
        }

        return introspect;
    }
}
