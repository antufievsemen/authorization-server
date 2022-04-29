package ru.spbstu.university.authorizationserver.service.auth;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.RedirectSettings;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.logout.LogoutRedirect;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.logout.LogoutResponse;
import ru.spbstu.university.authorizationserver.service.auth.security.codeverifier.CodeVerifierProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.OpenidTokenProvider;
import ru.spbstu.university.authorizationserver.service.exception.CodeVerifierNotValidException;

@Service
@AllArgsConstructor
public class LogoutManagerService {
    @NonNull
    private final LogoutInfoService logoutInfoService;
    @NonNull
    private final RedirectSettings settings;
    @NonNull
    private final UserService userService;
    @NonNull
    private final CodeVerifierProvider codeVerifierProvider;
    @NonNull
    private final OpenidTokenProvider openidTokenProvider;

    @NonNull
    public RedirectResponse logout(@NonNull Optional<String> redirectUri, @NonNull String logoutVerifier,
                                   @NonNull Optional<String> state) {
        final LogoutInfo logoutInfo = logoutInfoService.get(logoutVerifier)
                .orElseThrow(CodeVerifierNotValidException::new);

        userService.delete(logoutInfo.getUser().getId());

        return new LogoutResponse(redirectUri.orElse(settings.getLogout()), state.orElse(null));
    }

    @NonNull
    public RedirectResponse logout(@NonNull Optional<String> redirectUri, @NonNull Optional<String> state) {
        return new LogoutResponse(redirectUri.orElse(settings.getLogout()), state.orElse(null));
    }

    @NonNull
    public RedirectResponse logout(@NonNull User user, @NonNull String idTokenHint, @NonNull Optional<String> state,
                                   @NonNull Optional<String> redirectUri) {
        openidTokenProvider.introspect(idTokenHint);

        final String logoutVerifier = codeVerifierProvider.generate();
        logoutInfoService.create(logoutVerifier,
                new LogoutInfo(user, redirectUri.orElse(settings.getLogout()), state.orElse(null), idTokenHint));

        return new LogoutRedirect(settings.getLogout(), logoutVerifier);
    }
}
