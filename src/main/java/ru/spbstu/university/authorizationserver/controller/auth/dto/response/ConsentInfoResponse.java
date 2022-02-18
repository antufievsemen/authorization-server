package ru.spbstu.university.authorizationserver.controller.auth.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.params.AuthClient;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;

@Getter
@AllArgsConstructor
public class ConsentInfoResponse {
    @NonNull
    private final String subject;
    @NonNull
    private final AuthClient authClient;
    @NonNull
    private final List<String> responseTypes;
    @NonNull
    private final List<String> scopes;
    @NonNull
    private final String redirectUri;
    @NonNull
    private final List<GrantTypeEnum> grantTypes;
}
