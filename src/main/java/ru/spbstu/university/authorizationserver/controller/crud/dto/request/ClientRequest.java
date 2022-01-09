package ru.spbstu.university.authorizationserver.controller.crud.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class ClientRequest {
    @NotBlank(message = "Client id is required")
    private final String clientId;
    @NotBlank(message = "Client secret is required")
    private final String clientSecret;
    @NotNull(message = "Grant type is required")
    private final List<String> grantTypes;
    @Nullable
    private final List<String> scopes;
    @Nullable
    private final String redirectUri;
}
