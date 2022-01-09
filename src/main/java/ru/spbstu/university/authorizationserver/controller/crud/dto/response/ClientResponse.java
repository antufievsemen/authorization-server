package ru.spbstu.university.authorizationserver.controller.crud.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class ClientResponse {
    @NonNull
    private final String clientId;
    @NonNull
    private final String clientSecret;
    @NonNull
    private final List<String> grantTypes;
    @Nullable
    private final List<String> scope;
    @Nullable
    private final String redirectUri;
}
