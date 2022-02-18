package ru.spbstu.university.authorizationserver.service.auth.dto.redirect.consent;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.enums.RedirectResponseEnum;

@Getter
@AllArgsConstructor
public class ConsentRedirect implements RedirectResponse {
    @NonNull
    private final String challenge;
    @NonNull
    private final RedirectResponseEnum redirectResponseEnum = RedirectResponseEnum.CONSENT_VERIFIER;
    @NonNull
    private final String redirectUrl;

    @NonNull
    @Override
    public Map<String, Object> getRedirectAttributes() {
        return Map.of(redirectResponseEnum.getName(), challenge);
    }
}
