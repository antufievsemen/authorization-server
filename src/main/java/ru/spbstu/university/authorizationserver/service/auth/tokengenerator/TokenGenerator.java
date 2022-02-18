package ru.spbstu.university.authorizationserver.service.auth.tokengenerator;

import java.util.List;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;

public interface TokenGenerator {
    TokenResponseBody generate(Client client, String code, String redirectUri, List<GrantTypeEnum> grantTypes, String sessionId, String codeVerifier);
}
