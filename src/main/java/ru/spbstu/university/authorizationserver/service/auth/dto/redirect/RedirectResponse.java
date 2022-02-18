package ru.spbstu.university.authorizationserver.service.auth.dto.redirect;

import java.util.Map;
import lombok.NonNull;

public interface RedirectResponse {

    @NonNull
    String getRedirectUrl();

    @NonNull
    Map<String, Object> getRedirectAttributes();
}
