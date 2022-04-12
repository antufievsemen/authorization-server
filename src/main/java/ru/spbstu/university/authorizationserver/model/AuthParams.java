package ru.spbstu.university.authorizationserver.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthParams {
    @NonNull
    private ClientInfo clientInfo;
    @NonNull
    private List<ResponseTypeEnum> responseTypes;
    @NonNull
    private List<GrantTypeEnum> grantTypes;
    @NonNull
    private List<String> scopes;
    @NonNull
    private String redirectUri;
    @NonNull
    private String state;
    @NonNull
    private String sessionId;
    @Nullable
    private String nonce;
}
