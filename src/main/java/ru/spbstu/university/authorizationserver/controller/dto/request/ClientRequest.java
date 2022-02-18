package ru.spbstu.university.authorizationserver.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Getter
@AllArgsConstructor
public class ClientRequest {
    @JsonProperty(value = "client_id")
    @NotBlank(message = "Client id is required")
    private final String clientId;
    @JsonProperty(value = "client_secret")
    @NotBlank(message = "Client secret is required")
    private final String clientSecret;
    @JsonProperty(value = "grant_types")
    @NotNull(message = "Grant type is required")
    private final List<GrantTypeEnum> grantTypes;
    @NotNull(message = "At least one scope is required")
    private final List<String> scopes;
    @JsonProperty(value = "response_types")
    @NotNull(message = "At least one scope is required")
    private final List<ResponseTypeEnum> responseTypes;
    @NonNull
    private final List<String> callbacks;
}
