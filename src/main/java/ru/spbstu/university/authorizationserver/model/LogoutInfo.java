package ru.spbstu.university.authorizationserver.model;

import io.micrometer.core.lang.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class LogoutInfo {
    @NonNull
    private final String subject;
    @NonNull
    private final String clientId;
    @NonNull
    private final List<String> userScopes;
    @NonNull
    private final String sid;
    @NonNull
    private final LocalDateTime createdAt;
    @Setter
    @Nullable
    private String redirectUri;
}
