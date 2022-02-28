package ru.spbstu.university.authorizationserver.service.auth;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.RedirectSettings;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.service.ConsentParamsService;
import ru.spbstu.university.authorizationserver.service.AuthParamsService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.logout.LogoutRedirect;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.logout.LogoutResponse;
import ru.spbstu.university.authorizationserver.service.auth.security.codeverifier.CodeVerifierProvider;

@Service
@AllArgsConstructor
public class LogoutManagerService {
    @NonNull
    private final LogoutInfoService logoutInfoService;
    @NonNull
    private final RedirectSettings settings;
    @NonNull
    private final ConsentParamsService consentParamsService;
    @NonNull
    private final AuthParamsService authParamsService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final CodeVerifierProvider codeVerifierProvider;

    @NonNull
    public RedirectResponse logout(@NonNull String sessionId,
                                   @NonNull Optional<String> redirectUri,
                                   @NonNull Optional<String> logoutVerifier) {
        final Optional<User> user = userService.getBySessionId(sessionId);
        if (logoutVerifier.isEmpty() && user.isPresent()) {
            logoutInfoService.create(sessionId, new LogoutInfo(user.get(), redirectUri.orElse(null)));
            return new LogoutRedirect(codeVerifierProvider.generate(), settings.getLogout());
        }

        user.ifPresent(value -> userService.delete(value.getId()));
        consentParamsService.delete(sessionId);
        authParamsService.delete(sessionId);

        return new LogoutResponse(redirectUri.orElse(""));
    }
}
