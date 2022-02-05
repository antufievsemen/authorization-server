package ru.spbstu.university.authorizationserver.service.flow.dto.logout;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.flow.dto.RedirectResponse;

@Getter
@AllArgsConstructor
public class LogoutResponse implements RedirectResponse {
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
        return Map.of("logout_challenge", challenge);
    }
}
