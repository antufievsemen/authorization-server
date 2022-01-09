package ru.spbstu.university.authorizationserver.controller.crud.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class ClientRequest {
    @NonNull
    private final String clientId;
    @NonNull
    private final String clientSecret;
    @NonNull
    private final List<String> grantTypes;
    @Nullable
    private final List<String> scopes;
    @Nullable
    private final String redirectUri;
}
