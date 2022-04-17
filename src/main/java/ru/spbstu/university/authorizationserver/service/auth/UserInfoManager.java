package ru.spbstu.university.authorizationserver.service.auth;

import io.jsonwebtoken.Claims;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.controller.dto.response.UserInfoResponse;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.exception.SessionExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;

@Service
@AllArgsConstructor
public class UserInfoManager {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final UserService userService;

    @NonNull
    public UserInfoResponse getInfo(@NonNull String token, @NonNull String sessionId) {
        final User user = userService.getBySessionId(sessionId).orElseThrow(SessionExpiredException::new);
        final Claims claims = accessTokenProvider.validate(token, user.getClient().getClientId());

        return new UserInfoResponse(Map.of());
    }
}
