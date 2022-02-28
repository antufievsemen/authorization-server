package ru.spbstu.university.authorizationserver.controller;

import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.dto.request.IntrospectRequest;
import ru.spbstu.university.authorizationserver.controller.dto.request.RevokeRequest;
import ru.spbstu.university.authorizationserver.controller.dto.request.TokenRequest;
import ru.spbstu.university.authorizationserver.controller.dto.response.IntrospectResponse;
import ru.spbstu.university.authorizationserver.service.auth.IntrospectManager;
import ru.spbstu.university.authorizationserver.service.auth.RevokeManager;
import ru.spbstu.university.authorizationserver.service.auth.TokenManager;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;

@ApiV1
@RestController
@AllArgsConstructor
public class TokenController {
    @NonNull
    private final TokenManager tokenManager;
    @NonNull
    private final RevokeManager revokeManager;
    @NonNull
    private final IntrospectManager introspectManager;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/oauth2/token")
    public TokenResponseBody token(@RequestBody TokenRequest request, @NonNull HttpSession httpSession) {
        return tokenManager.generate(request.getClientId(), request.getClientSecret(), request.getCode(),
                request.getRedirectUri(), request.getGrantTypes(), request.getAccessToken(), request.getRefreshToken(), request.getCodeVerifier(),
                httpSession.getId(), request.getScopes());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/oauth2/revoke")
    public void revoke(@RequestBody RevokeRequest request) {
        revokeManager.revoke(request.getToken());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/oauth2/introspect")
    public IntrospectResponse introspect(@RequestBody IntrospectRequest request) {
        return introspectManager.introspect(request.getToken(), request.getScopes());
    }
}
