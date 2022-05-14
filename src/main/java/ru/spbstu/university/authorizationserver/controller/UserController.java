package ru.spbstu.university.authorizationserver.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.dto.response.UserInfoResponse;
import ru.spbstu.university.authorizationserver.service.auth.UserInfoManager;

@ApiV1
@RestController
@AllArgsConstructor
public class UserController {
    @NonNull
    private final UserInfoManager userInfoManager;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/userinfo")
    public UserInfoResponse userinfo(@RequestHeader("Authorization") String token) {
        return userInfoManager.getInfo(token);
    }
}