package ru.spbstu.university.authorizationserver.controller.flow.authcode;

import javax.servlet.http.HttpSession;
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
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.request.AcceptLoginRequest;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.response.LoginAcceptResponse;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.response.LoginInfoResponse;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.service.LoginAcceptService;


@ServerName
@RestController
@AllArgsConstructor
public class LoginAcceptController {
    @NonNull
    private final LoginAcceptService loginAcceptService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/auth/requests/login")
    public LoginInfoResponse get(@RequestParam(name = "login_challenge") String challenge) {
        return toResponse(loginAcceptService.getInfo(challenge));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/auth/requests/login/accept")
    public LoginAcceptResponse accept(@RequestParam(name = "login_challenge") String challenge,
                                      @NonNull @RequestBody AcceptLoginRequest request,
                                      @NonNull HttpSession httpSession) {

        return toAcceptResponse(loginAcceptService.accept(challenge, request.getSub(), httpSession.getId()));
    }

    @NonNull
    private LoginInfoResponse toResponse(@NonNull LoginAcceptService.LoginInfo loginInfo) {
        return new LoginInfoResponse(loginInfo.getRequestedResponseType(), loginInfo.getRequestedScopes(), loginInfo.getRequestedUrl(), loginInfo.getClientInfo());
    }

    @NonNull
    private LoginAcceptResponse toAcceptResponse(@NonNull RequestParams requestParams) {
        return new LoginAcceptResponse(new DefaultUriBuilderFactory("http://localhost:8080/v1/auth-server/auth").builder()
                .queryParams(requestParams.getMap()).build().toASCIIString());
    }
}
