package ru.spbstu.university.authorizationserver.service.auth;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.spbstu.university.authorizationserver.controller.dto.response.UserInfoResponse;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.IntrospectBody;
import ru.spbstu.university.authorizationserver.service.auth.exception.UserInfoNotFoundException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception.AccessTokenIsExpiredException;

@Service
@AllArgsConstructor
public class UserInfoManager {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final UserService userService;

    @NonNull
    public UserInfoResponse getInfo(@RequestHeader("Authorization") String token) {
        final IntrospectBody introspect = accessTokenProvider.introspect(token);
        if (!introspect.isActive()) {
            throw new AccessTokenIsExpiredException();
        }

        final Optional<Map<String, String>> userInfo = userService
                .getUserInfo(introspect.getClaims().getSubject());
        return new UserInfoResponse(userInfo.orElseThrow(UserInfoNotFoundException::new));
    }
}
