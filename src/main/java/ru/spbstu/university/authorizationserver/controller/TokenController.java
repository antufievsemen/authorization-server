package ru.spbstu.university.authorizationserver.controller;

import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.controller.dto.request.IntrospectRequest;
import ru.spbstu.university.authorizationserver.controller.dto.request.RevokeRequest;
import ru.spbstu.university.authorizationserver.controller.dto.request.TokenRequest;
import ru.spbstu.university.authorizationserver.controller.dto.response.IntrospectResponse;
import ru.spbstu.university.authorizationserver.controller.dto.response.TokenResponse;
import ru.spbstu.university.authorizationserver.service.token.TokenService;

@ServerName
@RestController
@AllArgsConstructor
public class TokenController {

    @NonNull
    private final TokenService tokenService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/oauth2/token")
    public TokenResponse token(@RequestBody TokenRequest request, @NonNull HttpSession httpSession) {
        return tokenService.generate(request.getClientId(), request.getClientSecret(), request.getCode(),
                request.getRedirectUri(), request.getGrantType(), request.getAccessToken(), request.getRefreshToken(), request.getCodeVerifier(),
                httpSession.getId());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/oauth2/revoke")
    public void revoke(@RequestBody RevokeRequest request) {
        tokenService.revoke(request.getToken());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/oauth2/introspect")
    public IntrospectResponse introspect(@RequestBody IntrospectRequest request) {
        return tokenService.introspect(request.getToken(), request.getScopes());
    }
}
