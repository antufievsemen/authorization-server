package ru.spbstu.university.authorizationserver.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.service.JwksService;

@RestController
@AllArgsConstructor
public class JwksController {
    @NonNull
    private final JwksService jwksService;

    @ResponseStatus
    @GetMapping("/.well-known/openid-configuration")
    public void openidConfiguration() {

    }

    @ResponseStatus
    @GetMapping("/.well-known/jwks.json")
    public void keys() {

    }
}
