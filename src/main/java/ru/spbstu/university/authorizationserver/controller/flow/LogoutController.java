package ru.spbstu.university.authorizationserver.controller.flow;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.controller.flow.dto.response.LogoutInfoResponse;
import ru.spbstu.university.authorizationserver.model.LogoutInfo;
import ru.spbstu.university.authorizationserver.service.LogoutService;

@ServerName
@RestController
@AllArgsConstructor
public class LogoutController {
    @NonNull
    private final LogoutService logoutService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth/requests/logout")
    public LogoutInfoResponse getInfo(@RequestParam(name = "logout_challenge") String logoutChallenge) {
        final LogoutInfo info = logoutService.getInfo(logoutChallenge);
        return toResponse(info);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/auth/requests/logout/accept")
    public String accept(@RequestParam(name = "logout_challenge") String logoutChallenge) {
        return toAcceptResponse(logoutService.accept(logoutChallenge));
    }

    @NonNull
    private LogoutInfoResponse toResponse(@NonNull LogoutInfo logoutInfo) {
        return new LogoutInfoResponse(logoutInfo.getSubject(), logoutInfo.getClientId(), logoutInfo.getUserScopes(),
                logoutInfo.getSid(), logoutInfo.getCreatedAt().getNano(), logoutInfo.getRedirectUri());
    }

    @NonNull
    private String toAcceptResponse(@NonNull LogoutService.AcceptLogoutResponse response) {
        return new DefaultUriBuilderFactory("http://localhost:8080/v1/auth-server/auth").builder()
                .queryParam("logout_verifier", response.getLogoutVerifier()).build().toASCIIString();
    }
}
