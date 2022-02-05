package ru.spbstu.university.authorizationserver.service.flow;

import io.jsonwebtoken.Claims;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.LoginConsentLogoutProviderSettings;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.LogoutInfo;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.service.LogoutInfoService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.encyption.openid.OpenidTokenEncryptorService;
import ru.spbstu.university.authorizationserver.service.flow.dto.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.logout.BackchannelLogoutResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.logout.LogoutResponse;
import ru.spbstu.university.authorizationserver.service.flow.exception.BadRedirectRequestException;
import ru.spbstu.university.authorizationserver.service.flow.exception.LogoutChallengeIsNotValid;
import ru.spbstu.university.authorizationserver.service.flow.exception.LogoutErrorUserNotIdentifierException;

@Service
@AllArgsConstructor
public class LogoutManagerService {
    @NonNull
    private final LogoutInfoService logoutInfoService;
    @NonNull
    private final LoginConsentLogoutProviderSettings settings;
    @NonNull
    private final OpenidTokenEncryptorService openidTokenEncryptorService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;

    @NonNull
    public LogoutInfo endFlow(@NonNull String sessionId, @NonNull String idTokenHint) {
        final Claims claims = openidTokenEncryptorService.validate(idTokenHint);
        final Optional<User> user = userService.get(claims.getSubject());
        return user.map(u -> new LogoutInfo(u.getSub(), u.getClient().getClientId(),
                        user.get().getScopes().stream().map(Scope::getName).collect(Collectors.toList()),
                        sessionId, user.get().getCreatedAt()))
                .orElseThrow(LogoutErrorUserNotIdentifierException::new);
    }

    @NonNull
    public LogoutInfo endFlow(@NonNull String sessionId) {
        final Optional<User> user = userService.get(sessionId);
        return user.map(u -> new LogoutInfo(u.getSub(), u.getClient().getClientId(),
                        user.get().getScopes().stream().map(Scope::getName).collect(Collectors.toList()),
                        sessionId, user.get().getCreatedAt()))
                .orElseThrow(LogoutErrorUserNotIdentifierException::new);
    }

    @NonNull
    public RedirectResponse endSession(@NonNull String sessionId, @NonNull Optional<String> idTokenHint,
                                       @NonNull Optional<String> redirectUri, @NonNull String referer,
                                       @NonNull Optional<String> logoutChallenge) {
        if (logoutChallenge.isPresent()) {
            final LogoutInfo logoutInfo = logoutInfoService.get(logoutChallenge.get()).orElseThrow(LogoutChallengeIsNotValid::new);
            logoutInfoService.delete(logoutChallenge.get());
            return new BackchannelLogoutResponse(Objects.requireNonNull(logoutInfo.getRedirectUri()));
        }

        final LogoutInfo logoutInfo = idTokenHint.map(s -> endFlow(sessionId, s))
                .orElseGet(() -> endFlow(sessionId));
        logoutInfo.setRedirectUri(referer);
        final Client client = clientService.getByClientId(logoutInfo.getClientId());
        if (Objects.nonNull(client.getCallback()) && redirectUri.isPresent()) {
            if (!client.getCallback().equalsIgnoreCase(redirectUri.get())) {
                throw new BadRedirectRequestException();
            }
            logoutInfo.setRedirectUri(redirectUri.get());
        }
        final String id = UUID.randomUUID().toString();
        logoutInfoService.create(id, logoutInfo);

        return new LogoutResponse(settings.getLogout(), id);
    }

    @NonNull
    public RedirectResponse initLogoutFlow(@NonNull LogoutInfo logoutInfo, @NonNull String redirectUri) {
        final String challenge = UUID.randomUUID().toString();
        logoutInfo.setRedirectUri(redirectUri);
        logoutInfoService.create(challenge, logoutInfo);

        return new LogoutResponse(redirectUri, challenge);
    }

    @NonNull
    public LogoutInfo getLogoutInfo(@NonNull String sessionId, @NonNull Optional<String> idTokenHint) {
        Optional<User> user = userService.get(sessionId);
        if (user.isPresent()) {
            final User userBody = user.get();
            return new LogoutInfo(userBody.getSub(), userBody.getClient().getClientId(),
                    userBody.getScopes().stream().map(Scope::getName).collect(Collectors.toList()),
                    userBody.getSessionId(), userBody.getCreatedAt());
        }
        if (idTokenHint.isPresent()) {
            final Claims claims = openidTokenEncryptorService.validate(idTokenHint.get());
            final Optional<User> userIdToken = userService.get(claims.getSubject());
            if (userIdToken.isPresent()) {
                final User userBody = userIdToken.get();
                return new LogoutInfo(userBody.getSub(), userBody.getClient().getClientId(),
                        userBody.getScopes().stream().map(Scope::getName).collect(Collectors.toList()),
                        userBody.getSessionId(), userBody.getCreatedAt());
            }
        }

        throw new LogoutErrorUserNotIdentifierException();
    }

    @NonNull
    public RedirectResponse fullLogout(String logoutVerifier) {
        final LogoutInfo logoutInfo = logoutInfoService.get(logoutVerifier).orElseThrow(LogoutChallengeIsNotValid::new);
        logoutInfoService.delete(logoutVerifier);

        userService.delete(logoutInfo.getSubject());
        return new BackchannelLogoutResponse(Objects.requireNonNull(logoutInfo.getRedirectUri()));
    }
}
