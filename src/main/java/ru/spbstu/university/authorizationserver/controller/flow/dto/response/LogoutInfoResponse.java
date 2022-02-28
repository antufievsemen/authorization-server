package ru.spbstu.university.authorizationserver.controller.flow.dto.response;

import io.micrometer.core.lang.Nullable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.Client;

@Getter
@AllArgsConstructor
public class LogoutInfoResponse {
    @NonNull
    private final String subject;
    @NonNull
    private final Client client;
    @Nullable
    private String redirectUri;
    private final int createdAt;
    @NonNull
    private final List<String> scopes;
}
