package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.clientcredentials;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.TokenResponseBody;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.exception.ScopeNotValidException;

@Component
@AllArgsConstructor
public class ClientCredentialsTokenResponseBodyGenerator {
    @NonNull
    private final AccessTokenProvider accessTokenProvider;

    @NonNull
    public TokenResponseBody generate(@NonNull Client client, @NonNull String sessionId,
                                      @Nullable List<String> scopes) {
        final ValidatedParams params = validateParams(client, scopes);
        final AccessTokenProvider.ClientCredentialsToken jwt = accessTokenProvider
                .createJwt(client.getClientId(), sessionId, params.getScopes());

        final LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("access_token", jwt.getToken());
        map.add("token_type", "JWT");
        map.add("expiresIn", String.valueOf(jwt.getExpiresIn().getTime()));
        map.put("scopes", jwt.getScopes());

        return new TokenResponseBody(map);
    }

    @NonNull
    private ValidatedParams validateParams(@NonNull Client client, @Nullable List<String> scopes) {
        if (Objects.isNull(scopes)) {
            return new ValidatedParams(client, List.of());
        }

        final List<String> availableScopes = client.getScopes().stream()
                .map(Scope::getName)
                .collect(Collectors.toList());

        if (!availableScopes.containsAll(scopes)) {
            throw new ScopeNotValidException();
        }

        return new ValidatedParams(client, scopes);
    }

    @Getter
    @AllArgsConstructor
    private static class ValidatedParams {
        @NonNull
        private final Client client;
        @NonNull
        private final List<String> scopes;
    }
}
