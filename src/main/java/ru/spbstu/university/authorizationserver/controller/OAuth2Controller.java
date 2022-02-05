package ru.spbstu.university.authorizationserver.controller;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.flow.AuthService;
import ru.spbstu.university.authorizationserver.service.flow.dto.RedirectResponse;

@ServerName
@RestController
@AllArgsConstructor
public class OAuth2Controller {
    @NonNull
    private final AuthService authService;

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping("/oauth2/auth")
    public RedirectView auth(@RequestParam(name = "responseTypeEnum") ResponseTypeEnum responseTypeEnum,
                             @RequestParam(name = "client_id") String clientId,
                             @RequestParam(name = "nonce") String nonce,
                             @RequestParam(name = "redirect_uri", required = false) Optional<String> redirectUri,
                             @RequestParam(name = "scope") List<String> scopes,
                             @RequestParam(name = "state") String state,
                             @RequestParam(name = "code_challenge", required = false) Optional<String> codeChallenge,
                             @RequestParam(name = "code_challenge_method", required = false) Optional<String> codeChallengeMethod,
                             @RequestParam(name = "consent_verifier", required = false) Optional<String> consentVerifier,
                             @NonNull HttpServletRequest request, @NonNull RedirectAttributes redirectAttributes) {
        final RedirectResponse redirectResponse = authService.authorize(clientId, responseTypeEnum, redirectUri, scopes, state,
                nonce, codeChallenge, codeChallengeMethod, request.getRequestedSessionId(), request.getHeader("referer"), consentVerifier);

        redirectAttributes.addAllAttributes(redirectResponse.getRedirectAttributes());
        return new RedirectView(redirectResponse.getRedirectUrl());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/sessions/logout")
    public RedirectView logout(@RequestParam(name = "id_token_hint", required = false) Optional<String> idTokenHint,
                               @RequestParam(name = "post_logout_redirect_uri", required = false) Optional<String> redirectUri,
                               @RequestParam(name = "logout_challenge", required = false) Optional<String> logoutVerifier,
                               @NonNull RedirectAttributes redirectAttributes, @NonNull HttpServletRequest request) {
        final RedirectResponse redirectResponse = authService.logout(request.getRequestedSessionId(), idTokenHint,
                redirectUri, request.getHeader("referer"), logoutVerifier);

        redirectAttributes.addAllAttributes(redirectResponse.getRedirectAttributes());
        return new RedirectView(redirectResponse.getRedirectUrl());
    }
}
