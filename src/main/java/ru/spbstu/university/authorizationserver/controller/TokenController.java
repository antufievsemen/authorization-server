package ru.spbstu.university.authorizationserver.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.service.TokenService;

@ServerName
@RestController
@AllArgsConstructor
public class TokenController {

    @NonNull
    private final TokenService tokenService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/oauth2/token")
    public void token() {

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/revoke")
    public void revoke(@RequestParam(name = "token") String token) {
    }
}
