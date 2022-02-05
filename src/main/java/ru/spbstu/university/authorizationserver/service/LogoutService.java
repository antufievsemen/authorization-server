package ru.spbstu.university.authorizationserver.service;

import java.util.Objects;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.LogoutInfo;
import ru.spbstu.university.authorizationserver.service.flow.exception.LogoutChallengeIsNotValid;

@Service
@Transactional
@AllArgsConstructor
public class LogoutService {
    @NonNull
    private final LogoutInfoService logoutInfoService;

    @NonNull
    public LogoutInfo getInfo(@NonNull String logoutChallenge) {
        return logoutInfoService.get(logoutChallenge).orElseThrow(LogoutChallengeIsNotValid::new);
    }

    @NonNull
    public AcceptLogoutResponse accept(@NonNull String logoutChallenge) {
        final LogoutInfo logoutInfo = logoutInfoService.get(logoutChallenge).orElseThrow(LogoutChallengeIsNotValid::new);
        logoutInfoService.delete(logoutChallenge);

        final String id = UUID.randomUUID().toString();
        logoutInfoService.create(id, logoutInfo);

        return new AcceptLogoutResponse(Objects.requireNonNull(logoutInfo.getRedirectUri()), id);
    }

    @Getter
    @AllArgsConstructor
    public static class AcceptLogoutResponse {
        @NonNull
        private final String redirectUri;
        @NonNull
        private final String logoutVerifier;
    }

}
