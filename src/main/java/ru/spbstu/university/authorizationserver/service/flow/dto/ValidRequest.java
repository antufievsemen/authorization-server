package ru.spbstu.university.authorizationserver.service.flow.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Setter
@Getter
@NoArgsConstructor
public class ValidRequest {
    @NonNull
    private String clientId;
    @NonNull
    private ResponseTypeEnum responseTypeEnum;
    @NonNull
    private String redirectUri;
    @NonNull
    private List<String> scopes;
    @NonNull
    private List<GrantTypesEnum> grantTypes;
}
