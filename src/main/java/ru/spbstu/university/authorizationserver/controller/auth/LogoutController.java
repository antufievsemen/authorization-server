package ru.spbstu.university.authorizationserver.controller.auth;

import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.controller.auth.dto.response.LogoutInfoResponse;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.service.auth.FlowSessionManagerService;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;

@ApiV1
@RestController
@AllArgsConstructor
public class LogoutController {
    @NonNull
    private final FlowSessionManagerService flowSessionManagerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth/requests/logout")
    public LogoutInfoResponse getInfo(@RequestParam(name = "logout_verifier") String codeVerifier, @NonNull HttpSession httpSession) {
        return getLogoutInfoResponse(flowSessionManagerService.getLogoutInfo(httpSession.getId(), codeVerifier));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/auth/requests/logout/accept")
    public String accept(@RequestParam(name = "logout_challenge") String logoutChallenge, @NonNull HttpSession httpSession) {
        return toAcceptResponse(flowSessionManagerService.accept(logoutChallenge, httpSession.getId()));
    }

    @NonNull
    private LogoutInfoResponse getLogoutInfoResponse(@NonNull LogoutInfo logoutInfo) {
        return new LogoutInfoResponse(logoutInfo.getUser().getId(), logoutInfo.getUser().getClient(),
                logoutInfo.getPostRedirectUri(), logoutInfo.getUser().getCreatedAt().getNano(),
                logoutInfo.getUser().getScopes().stream().map(Scope::getName).collect(Collectors.toList()));
    }

    @NonNull
    private String toAcceptResponse(@NonNull LogoutAccept logoutAccept) {
        return new DefaultUriBuilderFactory("http://localhost:8080/v1/auth-server/auth").builder()
                .queryParams(logoutAccept.getAttributes()).build().toASCIIString();
    }
}
