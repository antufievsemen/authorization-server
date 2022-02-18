package ru.spbstu.university.authorizationserver.service.auth.dto.redirect.logout;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.enums.RedirectResponseEnum;

@Getter
@AllArgsConstructor
public class LogoutRedirect implements RedirectResponse {
    @NonNull
    private final String redirectUri;
    @NonNull
    private final String challenge;

    @NonNull
    @Override
    public String getRedirectUrl() {
        return redirectUri;
    }

    @Override
    public @NonNull Map<String, Object> getRedirectAttributes() {
        return Map.of(RedirectResponseEnum.LOGOUT_VERIFIER.getName(), challenge);
    }
}
