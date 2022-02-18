package ru.spbstu.university.authorizationserver.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.service.JwksService;

@ApiV1
@RestController
@AllArgsConstructor
public class JwksController {
    @NonNull
    private final JwksService jwksService;

    /*
    Публичный ключ в бд со всей инфой
    Секрет хранить в файле
     */

    @ResponseStatus
    @GetMapping("/.well-known/openid-configuration")
    public void openidConfiguration() {

    }

    @ResponseStatus
    @GetMapping("/.well-known/jwks.json")
    public void keys() {

    }
}
