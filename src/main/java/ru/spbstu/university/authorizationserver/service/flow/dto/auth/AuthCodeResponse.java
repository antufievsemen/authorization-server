package ru.spbstu.university.authorizationserver.service.flow.dto.auth;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.flow.dto.AuthResponse;

@Getter
@AllArgsConstructor
public class AuthCodeResponse implements AuthResponse {
    @NonNull
    public final String code;
    @NonNull
    public final String redirectUrl;
    @NonNull
    public final String state;

    @NonNull
    @Override
    public Map<String, Object> getRedirectAttributes() {
        return Map.of("state", state, "code", code);
    }
}
