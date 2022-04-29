package ru.spbstu.university.authorizationserver.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponse {
    @NonNull
    private String accessToken;
    @NonNull
    private String tokenType;
    @NonNull
    private List<String> scope;
    @Nullable
    private String refreshToken;
    @Nullable
    private String idToken;
    @NonNull
    private String expiresIn;
}
