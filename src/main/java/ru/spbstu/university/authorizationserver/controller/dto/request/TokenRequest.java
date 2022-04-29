package ru.spbstu.university.authorizationserver.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;

@Getter
@AllArgsConstructor
public class TokenRequest {
    @NonNull
    private final String clientId;
    @NonNull
    private final List<GrantTypeEnum> grantTypes;
    @Nullable
    private final String clientSecret;
    @Nullable
    private final String code;
    @Nullable
    private final String accessToken;
    @Nullable
    private final String refreshToken;
    @Nullable
    private final String redirectUri;
    @Nullable
    private final List<String> scopes;
    @Nullable
    private final String codeVerifier;
}
