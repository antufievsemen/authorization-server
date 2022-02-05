package ru.spbstu.university.authorizationserver.service.flow.dto.consent;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.flow.dto.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.enums.ResponseRedirect;

@Getter
@AllArgsConstructor
public class ConsentResponse implements RedirectResponse {
    @NonNull
    private final String challenge;
    @NonNull
    private final ResponseRedirect responseRedirect = ResponseRedirect.CONSENT_CHALLENGE;
    @NonNull
    private final String redirectUrl;

    @NonNull
    @Override
    public Map<String, Object> getRedirectAttributes() {
        return Map.of(responseRedirect.getName(), challenge);
    }
}
