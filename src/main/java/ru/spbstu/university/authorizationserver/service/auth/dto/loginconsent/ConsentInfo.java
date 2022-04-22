package ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.cache.ClientInfo;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Getter
@AllArgsConstructor
public class ConsentInfo {
    @NonNull
    private final String subject;
    @NonNull
    private final ClientInfo clientInfo;
    @NonNull
    private final List<GrantTypeEnum> grantTypes;
    @NonNull
    private final List<ResponseTypeEnum> responseTypes;
    @NonNull
    private final List<String> scopes;
    @NonNull
    private final String redirectUri;
}
