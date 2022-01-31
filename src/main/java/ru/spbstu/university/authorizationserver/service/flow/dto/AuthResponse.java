package ru.spbstu.university.authorizationserver.service.flow.dto;

import java.util.Map;
import lombok.NonNull;

public interface AuthResponse {

    @NonNull
    String getRedirectUrl();

    @NonNull
    Map<String, Object> getRedirectAttributes();
}
