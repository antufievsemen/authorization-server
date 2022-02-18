package ru.spbstu.university.authorizationserver.service.auth;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.controller.dto.response.IntrospectResponse;
import ru.spbstu.university.authorizationserver.service.RevokeTokenService;
import ru.spbstu.university.authorizationserver.service.auth.exception.TokenRevokeException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.OpenidTokenProvider;

@Service
@AllArgsConstructor
public class IntrospectManager {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final OpenidTokenProvider openidTokenProvider;
    @NonNull
    private final RevokeTokenService revokeTokenService;

    @NonNull
    public IntrospectResponse introspect(@NonNull String token, @Nullable List<String> scopes) {
        final Optional<String> revokedToken = revokeTokenService.get(token);
        if (revokedToken.isPresent()) {
            throw new TokenRevokeException();
        }

        try {
            final Claims claims = accessTokenProvider.validate(token);
            final List<String> availableScopes = claims.get("scopes", List.class);

            if (Objects.nonNull(scopes)) {
                return new IntrospectResponse(availableScopes.containsAll(scopes), claims);
            }
            return new IntrospectResponse(true, claims);

        } catch (Exception e) {
            try {
                final Claims claims = openidTokenProvider.validate(token);
                return new IntrospectResponse(true, claims);
            } catch (Exception ex) {
                return new IntrospectResponse(false, Map.of());
            }
        }
    }
}
