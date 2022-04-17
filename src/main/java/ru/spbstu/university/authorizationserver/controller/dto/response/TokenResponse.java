package ru.spbstu.university.authorizationserver.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.enums.TokenType;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponse {
    @NonNull
    private String accessToken;
    @NonNull
    private TokenType tokenType;
    @Nullable
    private String refreshToken;
    @Nullable
    private String idToken;
    @NonNull
    private List<String> scopes;
    @NonNull
    private String redirectTo;
    private int expiresIn;
}
