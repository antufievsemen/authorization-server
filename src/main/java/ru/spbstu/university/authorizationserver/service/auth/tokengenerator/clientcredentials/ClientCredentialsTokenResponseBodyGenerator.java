package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.clientcredentials;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.exception.ClientCredentialsNotValidException;
import ru.spbstu.university.authorizationserver.service.exception.ScopeNotValidException;

@Component
@AllArgsConstructor
public class ClientCredentialsTokenResponseBodyGenerator {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;

    @NonNull
    public TokenResponseBody generate(@NonNull Client client, @Nullable String clientSecret,
                                      @NonNull String sessionId, @Nullable List<String> scopes) {
        validate(client, clientSecret, scopes);
        final TokenResponseBody tokenResponseBody = new TokenResponseBody(new LinkedMultiValueMap<>());
        final AccessTokenProvider.ClientCredentialsToken jwt = accessTokenProvider
                .createJwt(client.getClientId(), sessionId, Objects.requireNonNull(scopes));

        tokenResponseBody.getAttributes().add("access_token", jwt.getToken());
        tokenResponseBody.getAttributes().add("token_type", "JWT");
        tokenResponseBody.getAttributes().add("expiresIn", String.valueOf(jwt.getExpiresIn().getTime()));
        tokenResponseBody.getAttributes().put("scopes", jwt.getScopes());

        return tokenResponseBody;
    }

    private void validate(@NonNull Client client, @Nullable String clientSecret, @Nullable List<String> scopes) {
        if (Objects.isNull(clientSecret) || !client.getClientSecret().equals(clientSecret)) {
            throw new ClientCredentialsNotValidException();
        }

        final List<String> availableScopes = client.getScopes().stream()
                .map(Scope::getName)
                .collect(Collectors.toList());
        if (Objects.isNull(scopes) || !availableScopes.containsAll(scopes)) {
            throw new ScopeNotValidException();
        }
    }
}
