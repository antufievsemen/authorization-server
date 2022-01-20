package ru.spbstu.university.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class JwksController {

    @ResponseStatus
    @GetMapping("/.well-known/openid-configuration")
    public void openidConfiguration() {

    }

    @ResponseStatus
    @GetMapping("/.well-known/jwks.json")
    public void keys() {

    }
}
