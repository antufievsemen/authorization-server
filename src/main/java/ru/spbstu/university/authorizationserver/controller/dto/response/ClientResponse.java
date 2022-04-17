package ru.spbstu.university.authorizationserver.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientResponse {
    @NonNull
    private final String clientId;
    @NonNull
    private final String clientSecret;
    @NonNull
    private final List<String> grantTypes;
    @NonNull
    private final List<String> scopes;
    @NonNull
    private final List<String> responseTypes;
    @NonNull
    private final List<String> callbacks;
}
