package ru.spbstu.university.authorizationserver.model.params;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class AuthParams {
    @NonNull
    private AuthClient authClient;
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
    private String nonce;
    @NonNull
    private String sessionId;
    @Nullable
    private PkceParams pkceParams;
    @Setter
    @Nullable
    private String subject;

    public void setPkceParams(@NonNull String codeChallenge, @NonNull String method) {
        this.pkceParams = new PkceParams(codeChallenge, method);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PkceParams {
        @NonNull
        private String codeChallenge;
        @NonNull
        private String method;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBody {
        @NonNull
        private String subject;
        @NonNull
        private List<String> scopes;
    }
}
