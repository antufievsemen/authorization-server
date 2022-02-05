package ru.spbstu.university.authorizationserver.controller.flow.dto.response;

import io.micrometer.core.lang.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class LogoutInfoResponse {
    @NonNull
    private final String subject;
    @NonNull
    private final String clientId;
    @NonNull
    private final List<String> userScopes;
    @NonNull
    private final String sid;
    private final int createdAt;
    @Setter
    @Nullable
    private String redirectUri;
}
