package ru.spbstu.university.authorizationserver.controller.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.params.AuthClient;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;

@Getter
@AllArgsConstructor
public class LoginInfoResponse {
    @NonNull
    @JsonProperty("client")
    private final AuthClient authClient;
    @NonNull
    @JsonProperty("response_types")
    private final List<String> responseTypes;
    @NonNull
    @JsonProperty("scopes")
    private final List<String> scopes;
    @NonNull
    @JsonProperty("redirect_uri")
    private final String redirectUri;
    @NonNull
    @JsonProperty("grant_types")
    private final List<GrantTypeEnum> grantTypes;
}
