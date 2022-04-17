package ru.spbstu.university.authorizationserver.service.auth.security.token.openid;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.model.KeySet;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.TokenEnum;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.security.JwksService;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception.AccessTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception.OpenidTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception.OpenidTokenWithNotExistUserException;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotFoundException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
public class OpenidTokenProvider {
    @NonNull
    private final UserService userService;
    @NonNull
    private final Generator<String> idGenerator;
    private final long VALIDITY_TIME_IN_MS;
    @NonNull
    private final SelfIssuerSettings settings;
    @NonNull
    private final JwksService jwksService;

    @SneakyThrows
    @Autowired
    public OpenidTokenProvider(@NonNull UserService userService, @NonNull Generator<String> idGenerator,
                               @NonNull SelfIssuerSettings settings, @NonNull JwksService jwksService) {
        this.userService = userService;
        this.idGenerator = idGenerator;
        this.settings = settings;
        this.jwksService = jwksService;
        this.VALIDITY_TIME_IN_MS = 1000 * 60 * 10;
    }

    @NonNull
    public String create(@NonNull String sub, @Nullable String nonce, @NonNull String sessionId,
                         @NonNull String accessTokenHash, @Nullable Map<String, String> claims) {
        final User user = userService.get(sub).orElseThrow(OpenidTokenWithNotExistUserException::new);
        final KeySet keySet = jwksService.getByClientId(user.getClient().getClientId(), TokenEnum.ACCESS_TOKEN)
                .orElseThrow(ClientNotFoundException::new);
        final Map<String, Object> map = new HashMap<>();
        Optional.ofNullable(claims).ifPresent(map::putAll);
        map.put("at_hash", accessTokenHash);
        Optional.ofNullable(nonce).ifPresent(s -> map.put("nonce", s));
        map.put("sid", sessionId);
        map.put("auth_time", LocalDateTime.now().getNano());
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        return Jwts.builder()
                .setClaims(map)
                .setSubject(sub)
                .setAudience("auth-code-client")
                .setIssuer(settings.getIssuer())
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keySet.getPrivateKey())
                .compact();
    }

    @NonNull
    public Claims validate(@NonNull String token, @NonNull String accessTokenHash,
                           @NonNull String clientId) throws OpenidTokenNotValidException {
        final KeySet keySet = jwksService.getByClientId(clientId, TokenEnum.ACCESS_TOKEN)
                .orElseThrow(ClientNotFoundException::new);
        Jws<Claims> claims = getClaims(token, keySet);

        final String at_hash = (String) claims.getBody().get("at_hash");
        if (!at_hash.equals(accessTokenHash)) {
            throw new OpenidTokenNotValidException();
        }

        return claims.getBody();
    }

    @NonNull
    public Claims validate(@NonNull String token, @NonNull String clientId) {
        final KeySet keySet = jwksService.getByClientId(clientId, TokenEnum.ACCESS_TOKEN)
                .orElseThrow(ClientNotFoundException::new);
        Jws<Claims> claims = getClaims(token, keySet);

        return claims.getBody();
    }

    @NonNull
    private Jws<Claims> getClaims(@NonNull String token, @NonNull KeySet keySet) {
        try {
            return Jwts.parser().setSigningKey(keySet.getPrivateKey()).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AccessTokenNotValidException();
        }
    }
}
