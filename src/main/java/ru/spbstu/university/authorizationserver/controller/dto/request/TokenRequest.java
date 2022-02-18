package ru.spbstu.university.authorizationserver.controller.dto.request;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;

@Getter
@AllArgsConstructor
public class TokenRequest {
    @Nullable
    private final String code;
    @NonNull
    private final String clientId;
    @Nullable
    private final String clientSecret;
    @NonNull
    private final List<GrantTypeEnum> grantTypes;
    @NonNull
    private final Optional<String> codeVerifier;
    @Nullable
    private final String accessToken;
    @Nullable
    private final String refreshToken;
    @Nullable
    private final String redirectUri;
    @Nullable
    private final List<String> scopes;
}
