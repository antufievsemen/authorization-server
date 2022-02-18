package ru.spbstu.university.authorizationserver.controller.auth;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.auth.dto.request.AcceptLoginRequest;
import ru.spbstu.university.authorizationserver.controller.auth.dto.response.LoginAcceptResponse;
import ru.spbstu.university.authorizationserver.controller.auth.dto.response.LoginInfoResponse;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.auth.FlowSessionManagerService;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.LoginAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.LoginInfo;

@ApiV1
@RestController
@AllArgsConstructor
public class LoginController {
    @NonNull
    private final FlowSessionManagerService flowSessionManagerService;
    @NonNull
    private final SelfIssuerSettings settings;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth/requests/login")
    public LoginInfoResponse get(@RequestParam(name = "login_verifier") String loginVerifier) {
        return getLoginInfoResponse(flowSessionManagerService.getLoginInfo(loginVerifier));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/auth/requests/login/accept")
    public LoginAcceptResponse accept(@RequestParam(name = "login_verifier") String loginVerifier,
                                      @NonNull @RequestBody AcceptLoginRequest request) {
        return getLoginAcceptResponse(flowSessionManagerService.acceptLogin(loginVerifier, request.getSubject()));
    }

    @NonNull
    private LoginInfoResponse getLoginInfoResponse(@NonNull LoginInfo loginInfo) {
        return new LoginInfoResponse(loginInfo.getAuthClient(),
                loginInfo.getResponseTypes().stream().map(ResponseTypeEnum::getName).collect(Collectors.toList()),
                loginInfo.getScopes(), loginInfo.getRedirectUri(), loginInfo.getGrantTypes());
    }

    @NonNull
    private LoginAcceptResponse getLoginAcceptResponse(@NonNull LoginAccept loginAccept) {
        return new LoginAcceptResponse(new DefaultUriBuilderFactory(settings.getIssuer())
                .uriString("/v1/oauth2/auth")
                .queryParams(loginAccept.getAttributes())
                .build().toASCIIString());
    }
}
