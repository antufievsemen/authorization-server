package ru.spbstu.university.authorizationserver.service.auth;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.service.RevokeTokenService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.exception.SessionExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;

@Service
@AllArgsConstructor
public class RevokeManager {
    @NonNull
    private final RevokeTokenService revokeTokenService;
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final UserService userService;

    public void revoke(@NonNull String token, @NonNull String sessionId) {
        final User user = userService.getBySessionId(sessionId).orElseThrow(SessionExpiredException::new);
        final Claims claims = accessTokenProvider.validate(token, user.getClient().getClientId());

        revokeTokenService.save(token, token, claims.getExpiration());
    }
}
