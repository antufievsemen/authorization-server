package ru.spbstu.university.authorizationserver.service.auth;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.service.RevokeTokenService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.IntrospectBody;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;

@Service
@AllArgsConstructor
public class RevokeManager {
    @NonNull
    private final RevokeTokenService revokeTokenService;
    @NonNull
    private final AccessTokenProvider accessTokenProvider;

    public void revoke(@NonNull String token) {
        final IntrospectBody introspect = accessTokenProvider.introspect(token);

        revokeTokenService.revoke(token, introspect.getClaims(), introspect.getClaims().getExpiration());
    }
}
