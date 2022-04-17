package ru.spbstu.university.authorizationserver.controller.flow;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.config.RedirectSettings;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.flow.dto.response.LogoutInfoResponse;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.service.auth.FlowSessionManager;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;

@ApiV1
@RestController
@AllArgsConstructor
public class LogoutController {
    @NonNull
    private final FlowSessionManager flowSessionManager;
    @NonNull
    private final RedirectSettings settings;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth/requests/logout")
    public LogoutInfoResponse getInfo(@RequestParam(name = "logout_verifier") String codeVerifier) {
        return getLogoutInfoResponse(flowSessionManager.getLogoutInfo(codeVerifier));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/auth/requests/logout/accept")
    public String accept(@RequestParam(name = "logout_challenge") String logoutChallenge) {
        return toAcceptResponse(flowSessionManager.accept(logoutChallenge));
    }

    @NonNull
    private LogoutInfoResponse getLogoutInfoResponse(@NonNull LogoutInfo logoutInfo) {
        return new LogoutInfoResponse(logoutInfo.getUser().getId(), logoutInfo.getUser().getClient(),
                logoutInfo.getPostRedirectUri(), logoutInfo.getUser().getCreatedAt().getNano(),
                logoutInfo.getUser().getScopes().stream().map(Scope::getName).collect(Collectors.toList()));
    }

    @NonNull
    private String toAcceptResponse(@NonNull LogoutAccept logoutAccept) {
        return new DefaultUriBuilderFactory(settings.getLogout())
                .uriString("/v1/oauth2/auth")
                .queryParams(logoutAccept.getAttributes())
                .build().toASCIIString();
    }
}
