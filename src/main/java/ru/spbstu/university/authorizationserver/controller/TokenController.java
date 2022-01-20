package ru.spbstu.university.authorizationserver.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.service.token.access.AccessTokenService;
import ru.spbstu.university.authorizationserver.service.token.refresh.RefreshTokenService;

@ServerName
@RestController
@AllArgsConstructor
public class TokenController {

    @NonNull
    private final AccessTokenService accessTokenService;
    @NonNull
    private final RefreshTokenService refreshTokenService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/oauth2/token")
    public String token() {
        return accessTokenService.createJwt("awd", "wadaw", "wdawaw", null);
//        return accessTokenService.create("awdaw");
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/revoke")
    public void revoke(@RequestParam(name = "token") String token) {
    }
}
