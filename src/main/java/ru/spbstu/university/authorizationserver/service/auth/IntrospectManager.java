package ru.spbstu.university.authorizationserver.service.auth;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.service.RevokeTokenService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.IntrospectBody;
import ru.spbstu.university.authorizationserver.service.auth.exception.SessionExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.exception.TokenRevokeException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.OpenidTokenProvider;

@Service
@AllArgsConstructor
public class IntrospectManager {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final OpenidTokenProvider openidTokenProvider;//tbd
    @NonNull
    private final RevokeTokenService revokeTokenService;
    @NonNull
    private final UserService userService;

    @NonNull
    public IntrospectBody introspect(@NonNull String token, @Nullable List<String> scopes,
                                     @NonNull String sessionId) {
        final User user = userService.getBySessionId(sessionId).orElseThrow(SessionExpiredException::new);

        final Optional<String> revokedToken = revokeTokenService.get(token);
        if (revokedToken.isPresent()) {
            throw new TokenRevokeException();
        }

        final Claims claims = accessTokenProvider.validate(token, user.getClient().getClientId(), scopes);
        return new IntrospectBody(claims);
    }
}
