package ru.spbstu.university.authorizationserver.service.auth;

import io.jsonwebtoken.Claims;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.controller.dto.response.UserInfoResponse;
import ru.spbstu.university.authorizationserver.model.enums.ScopeEnum;
import ru.spbstu.university.authorizationserver.model.ConsentParams;
import ru.spbstu.university.authorizationserver.service.ConsentParamsService;
import ru.spbstu.university.authorizationserver.service.auth.exception.SessionExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.exception.UserinfoPermittedException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;

@Service
@AllArgsConstructor
public class UserInfoManager {
    @NonNull
    private final ConsentParamsService consentParamsService;
    @NonNull
    private final AccessTokenProvider accessTokenProvider;

    @NonNull
    public UserInfoResponse getInfo(@NonNull String token, @NonNull String sessionId) {
        final Claims claims = accessTokenProvider.validate(token);
        final ConsentParams consentParams = consentParamsService
                .get(sessionId).orElseThrow(SessionExpiredException::new);

        if (!Objects.requireNonNull(consentParams.getAuthParams().getSubject()).equals(claims.getSubject())) {
            throw new UserNotFoundException();
        }
        if (!Objects.requireNonNull(consentParams.getScopes())
                .contains(ScopeEnum.OPENID.getName())) {
            throw new UserinfoPermittedException();
        }

        return new UserInfoResponse(Objects.requireNonNull(consentParams.getUserinfo()));
    }
}
