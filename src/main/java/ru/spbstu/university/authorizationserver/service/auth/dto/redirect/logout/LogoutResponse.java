package ru.spbstu.university.authorizationserver.service.auth.dto.redirect.logout;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;

@Getter
@AllArgsConstructor
public class LogoutResponse implements RedirectResponse {
    @NonNull
    private final String redirectUri;

    @NonNull
    @Override
    public String getRedirectUrl() {
        return redirectUri;
    }

    @NonNull
    @Override
    public Map<String, Object> getRedirectAttributes() {
        return Map.of();
    }
}
