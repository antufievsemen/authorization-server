package ru.spbstu.university.authorizationserver.controller;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.service.auth.AuthProvider;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;

@ApiV1
@RestController
@AllArgsConstructor
public class AuthorizationController {
    @NonNull
    private final AuthProvider authProvider;

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping("/oauth2/auth")
    public RedirectView auth(@RequestParam(name = "response_types") List<String> responseTypes,
                             @RequestParam(name = "client_id") String clientId,
                             @RequestParam(name = "nonce", required = false) String nonce,
                             @RequestParam(name = "redirect_uri") String redirectUri,
                             @RequestParam(name = "scopes") List<String> scopes,
                             @RequestParam(name = "state") String state,
                             @RequestParam(name = "code_challenge", required = false) Optional<String> codeChallenge,
                             @RequestParam(name = "code_challenge_method", required = false) Optional<String> codeChallengeMethod,
                             @RequestParam(name = "login_verifier", required = false) Optional<String> loginVerifier,
                             @RequestParam(name = "consent_verifier", required = false) Optional<String> consentVerifier,
                             @NonNull HttpSession httpSession, @NonNull RedirectAttributes redirectAttributes) {
        final RedirectResponse redirectResponse = authProvider.authorize(clientId,
                responseTypes, redirectUri, scopes, state, nonce, codeChallenge, codeChallengeMethod,
                httpSession.getId(), consentVerifier, loginVerifier);

        redirectAttributes.addAllAttributes(redirectResponse.getRedirectAttributes());
        return new RedirectView(redirectResponse.getRedirectUrl());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/sessions/logout")
    public RedirectView logout(@RequestParam(name = "post_logout_redirect_uri", required = false) Optional<String> redirectUri,
                               @RequestParam(name = "logout_verifier", required = false) Optional<String> logoutVerifier,
                               @RequestParam(name = "id_token_hint") String idTokenHint,
                               @RequestParam(name = "state", required = false) Optional<String> state,
                               @NonNull RedirectAttributes redirectAttributes, @NonNull HttpSession httpSession) {
        final RedirectResponse redirectResponse = authProvider.logout(httpSession.getId(), redirectUri, logoutVerifier,
                idTokenHint, state);

        redirectAttributes.addAllAttributes(redirectResponse.getRedirectAttributes());
        return new RedirectView(redirectResponse.getRedirectUrl());
    }
}
