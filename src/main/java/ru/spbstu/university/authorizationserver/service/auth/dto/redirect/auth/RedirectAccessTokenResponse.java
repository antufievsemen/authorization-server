package ru.spbstu.university.authorizationserver.service.auth.dto.redirect.auth;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;

@Getter
@AllArgsConstructor
public class RedirectAccessTokenResponse implements RedirectResponse {
    @NonNull
    private final String state;
    @NonNull
    private final String accessToken;
    @NonNull
    private final Date expiresIn;
    @NonNull
    private final List<String> scopes;
    @NonNull
    private final String redirectUrl;

    @NonNull
    @Override
    public String getRedirectUrl() {
        return redirectUrl;
    }

    @NonNull
    @Override
    public Map<String, Object> getRedirectAttributes() {
        return Map.of("access_token", accessToken,
                "token_type", "JWT",
                "state", state,
                "scopes", scopes,
                "expires_in", expiresIn.toInstant().getNano());
    }
}
