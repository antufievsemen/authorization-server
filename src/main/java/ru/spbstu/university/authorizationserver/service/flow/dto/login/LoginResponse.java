package ru.spbstu.university.authorizationserver.service.flow.dto.login;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.flow.dto.AuthResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.enums.ResponseRedirect;

@Getter
@AllArgsConstructor
public class LoginResponse implements AuthResponse {
    @NonNull
    private final String challenge;
    @NonNull
    private final ResponseRedirect responseRedirect = ResponseRedirect.LOGIN_CHALLENGE;
    @NonNull
    private final String redirectUrl;

    @NonNull
    @Override
    public Map<String, Object> getRedirectAttributes() {
        return null;
    }
}
