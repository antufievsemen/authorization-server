package ru.spbstu.university.authorizationserver.service.auth;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;
import ru.spbstu.university.authorizationserver.service.auth.exception.PostTokenBadRequest;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken.RefreshAccessTokenResponseBodyGenerator;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.authcode.AuthCodeTokenResponseBodyGenerator;
import ru.spbstu.university.authorizationserver.service.auth.tokengenerator.clientcredentials.ClientCredentialsTokenResponseBodyGenerator;

@Service
@AllArgsConstructor
public class TokenManager {
    @NonNull
    private final AuthCodeTokenResponseBodyGenerator authCodeTokenResponseBodyGenerator;
    @NonNull
    private final ClientCredentialsTokenResponseBodyGenerator clientCredentialsTokenResponseBodyGenerator;
    @NonNull
    private final RefreshAccessTokenResponseBodyGenerator refreshAccessTokenResponseBodyGenerator;
    @NonNull
    private final ClientService clientService;

    @NonNull
    public TokenResponseBody generate(@NonNull String clientId, @Nullable String clientSecret, @Nullable String code,
                                      @Nullable String redirectUri, @NonNull List<GrantTypeEnum> grantTypes,
                                      @Nullable String accessToken, @Nullable String refreshToken,
                                      @NonNull String sessionId, @Nullable List<String> scopes,
                                      @Nullable String codeVerifier) {
        if (grantTypes.contains(GrantTypeEnum.REFRESH_TOKEN)
                && !grantTypes.contains(GrantTypeEnum.AUTHORIZATION_CODE)
                && !grantTypes.contains(GrantTypeEnum.CLIENT_CREDENTIALS)) {
            return refreshAccessTokenResponseBodyGenerator.generate(accessToken, refreshToken, clientId);
        }

        if (Objects.isNull(clientSecret)) {
            throw new PostTokenBadRequest();
        }
        final Client client = clientService.getByClientIdAndSecret(clientId, clientSecret);

        if (grantTypes.contains(GrantTypeEnum.AUTHORIZATION_CODE)
                && !grantTypes.contains(GrantTypeEnum.CLIENT_CREDENTIALS)) {
            return authCodeTokenResponseBodyGenerator.generate(client, code, redirectUri, grantTypes, sessionId,
                    codeVerifier);
        }

        if (grantTypes.contains(GrantTypeEnum.CLIENT_CREDENTIALS)
                && !grantTypes.contains(GrantTypeEnum.AUTHORIZATION_CODE)
                && !grantTypes.contains(GrantTypeEnum.REFRESH_TOKEN)) {
            return clientCredentialsTokenResponseBodyGenerator.generate(client, sessionId, scopes);
        }

        throw new PostTokenBadRequest();
    }
}
