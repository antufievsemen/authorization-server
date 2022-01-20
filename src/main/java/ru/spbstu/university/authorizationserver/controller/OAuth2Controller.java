package ru.spbstu.university.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OAuth2Controller {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth")
    public void auth() {

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/sessions/logout")
    public void logout() {

    }
}
