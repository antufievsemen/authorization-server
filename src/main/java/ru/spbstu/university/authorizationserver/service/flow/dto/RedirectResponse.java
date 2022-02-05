package ru.spbstu.university.authorizationserver.service.flow.dto;

import java.util.Map;
import lombok.NonNull;

public interface RedirectResponse {

    @NonNull
    String getRedirectUrl();

    @NonNull
    Map<String, Object> getRedirectAttributes();
}
