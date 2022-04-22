package ru.spbstu.university.authorizationserver.controller.flow.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.cache.ClientInfo;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConsentInfoResponse {
    @NonNull
    private final String subject;
    @NonNull
    private final ClientInfo clientInfo;
    @NonNull
    private final List<String> responseTypes;
    @NonNull
    private final List<String> scopes;
    @NonNull
    private final String redirectUri;
    @NonNull
    private final List<GrantTypeEnum> grantTypes;
}
