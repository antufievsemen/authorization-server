package ru.spbstu.university.authorizationserver.controller.auth.dto.requestparam;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Getter
@Setter
@NoArgsConstructor
public class AuthUrlParams {
    private List<ResponseTypeEnum> responseTypes;
    private String clientId;
    private String nonce;
    private List<String> scopes;
    private String state;
    private String redirectUri;

    private Optional<String> codeChallenge;
    private Optional<String> codeChallengeMethod;
    private Optional<String> loginVerifier;
    private Optional<String> consentVerifier;
}
