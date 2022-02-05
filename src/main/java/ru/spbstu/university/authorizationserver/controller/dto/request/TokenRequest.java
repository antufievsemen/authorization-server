package ru.spbstu.university.authorizationserver.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;

@Getter
@AllArgsConstructor
public class TokenRequest {
    @Nullable
    private final String code;
    @NonNull
    private final String clientId;
    @NonNull
    private final String clientSecret;
    @NonNull
    private final GrantTypesEnum grantType;
    @Nullable
    private final String codeVerifier;
    @Nullable
    private final String accessToken;
    @Nullable
    private final String refreshToken;
    @NonNull
    private final String redirectUri;
}
