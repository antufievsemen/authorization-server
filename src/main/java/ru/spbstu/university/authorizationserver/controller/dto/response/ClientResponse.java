package ru.spbstu.university.authorizationserver.controller.dto.response;

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
    @NonNull
    private final List<String> scope;
    @NonNull
    private final List<String> responseTypes;
    @Nullable
    private final String redirectUri;
}
