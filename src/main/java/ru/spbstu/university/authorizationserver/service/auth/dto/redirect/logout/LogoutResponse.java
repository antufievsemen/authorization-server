package ru.spbstu.university.authorizationserver.service.auth.dto.redirect.logout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;

@Getter
@AllArgsConstructor
public class LogoutResponse implements RedirectResponse {
    @NonNull
    private final String redirectUri;
    @Nullable
    private final String state;

    @NonNull
    @Override
    public String getRedirectUrl() {
        return redirectUri;
    }

    @NonNull
    @Override
    public Map<String, Object> getRedirectAttributes() {
        final Map<String, Object> map = new HashMap<>();

        if (Objects.nonNull(state)) {
            map.put("state", state);
        }

        return map;
    }
}
