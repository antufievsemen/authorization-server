package ru.spbstu.university.authorizationserver.controller;

import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.dto.response.UserInfoResponse;
import ru.spbstu.university.authorizationserver.service.auth.TokenManager;
import ru.spbstu.university.authorizationserver.service.auth.UserInfoManager;

@ApiV1
@RestController
@AllArgsConstructor
public class UserInfoController {
    @NonNull
    private final UserInfoManager userInfoManager;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/userinfo")
    public UserInfoResponse userinfo(@RequestParam("token") String token, @NonNull HttpSession httpSession) {
        return userInfoManager.getInfo(token, httpSession.getId());
    }
}