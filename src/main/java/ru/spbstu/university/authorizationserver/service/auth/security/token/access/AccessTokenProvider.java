package ru.spbstu.university.authorizationserver.service.auth.security.token.access;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.model.KeySet;
import ru.spbstu.university.authorizationserver.model.enums.TokenEnum;
import ru.spbstu.university.authorizationserver.model.enums.TokenType;
import ru.spbstu.university.authorizationserver.service.auth.security.JwksService;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception.AccessTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotFoundException;
import ru.spbstu.university.authorizationserver.service.exception.ScopeNotValidException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Slf4j
@Service
public class AccessTokenProvider {

    @NonNull
    private final Generator<String> idGenerator;
    private final int VALIDITY_TIME_IN_MS;
    @NonNull
    private final SelfIssuerSettings settings;
    @NonNull
    private final JwksService jwksService;


    @SneakyThrows
    @Autowired
    public AccessTokenProvider(@NonNull Generator<String> idGenerator, @NonNull SelfIssuerSettings settings,
                               @NonNull JwksService jwksService) {
        this.idGenerator = idGenerator;
        this.settings = settings;
        this.jwksService = jwksService;
        this.VALIDITY_TIME_IN_MS = 3_600_000;
    }

    @SneakyThrows
    @NonNull
    public TokenInfo createJwt(@NonNull String sub, @NonNull String clientId, @NonNull String sessionId,
                               @Nullable List<String> aud, @NonNull List<String> scopes) {
        final Map<String, Object> map = new HashMap<>();
        map.put("clientId", clientId);
        map.put("token_use", "access_token");
        map.put("scopes", scopes);
        map.put("sid", sessionId);
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final String token = Jwts.builder()
                .setClaims(map)
                .setSubject(sub)
                .setAudience(aud != null ? aud.toString() : "")
                .setIssuer(settings.getIssuer())
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, getPrivateKey(clientId))
                .compact();

        return new TokenInfo(sub, token, expiredTime, scopes, TokenType.JWT);
    }

    @SneakyThrows
    @NonNull
    public ClientCredentialsToken createJwt(@NonNull String clientId, @NonNull String sessionId,
                                            @NonNull List<String> scopes) {
        final Map<String, Object> map = new HashMap<>();
        map.put("clientId", clientId);
        map.put("token_use", "access_token");
        map.put("scopes", scopes);
        map.put("sid", sessionId);
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final String token = Jwts.builder()
                .setClaims(map)
                .setIssuer(settings.getIssuer())
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, getPrivateKey(clientId))
                .compact();

        return new ClientCredentialsToken(token, expiredTime, scopes, TokenType.JWT);
    }

    @SneakyThrows
    @NonNull
    public TokenInfo createJwt(@NonNull Claims claims, @NonNull String clientId) {
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiredTime)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, getPrivateKey(clientId))
                .compact();

        return new TokenInfo(claims.getSubject(), token, expiredTime, claims.get("scopes", List.class),
                TokenType.JWT);
    }

    @NonNull
    public Claims validate(@NonNull String token, @NonNull String clientId) {
        final KeySet keySet = jwksService.getByClientId(clientId, TokenEnum.ACCESS_TOKEN)
                .orElseThrow(ClientNotFoundException::new);
        Jws<Claims> claims = getClaims(token, keySet);

        return claims.getBody();
    }

    @NonNull
    public Claims validate(@NonNull String token, @NonNull String clientId, @Nullable List<String> scopes) {
        final KeySet keySet = jwksService.getByClientId(clientId, TokenEnum.ACCESS_TOKEN)
                .orElseThrow(ClientNotFoundException::new);
        Jws<Claims> claims = getClaims(token, keySet);

        if (Objects.nonNull(scopes)) {
            final List<String> tokenScopes = claims.getBody().get("scopes", List.class);
            if (!tokenScopes.containsAll(scopes)) {
                throw new ScopeNotValidException();
            }
        }

        return claims.getBody();
    }

    @SneakyThrows
    public String getHash(@NonNull String token) {
        final byte[] bytes = DigestUtils.sha256(token);
        final byte[] hashBytes = new byte[16];
        System.arraycopy(bytes, 0, hashBytes, 0, 15);

        return Arrays.toString(hashBytes);
    }

    @NonNull
    public Jws<Claims> getClaims(@NonNull String token, @NonNull KeySet keySet) {
        try {
            return Jwts.parser().setSigningKey(keySet.getPrivateKey()).parseClaimsJws(token);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                | SignatureException | IllegalArgumentException e) {
            throw new AccessTokenNotValidException();
        }
    }

    @SneakyThrows
    @NonNull
    private PrivateKey getPrivateKey(@NonNull String clientId) {
        final KeySet keySet = jwksService.getOrCreateKeyPair(clientId, TokenEnum.ACCESS_TOKEN);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keySet.getPrivateKey());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        @NonNull
        private final String subject;
        @NonNull
        private final String token;
        @NonNull
        private final Date expiresIn;
        @NonNull
        private final List<String> scopes;
        @NonNull
        private final TokenType tokenType;
    }

    @Getter
    @AllArgsConstructor
    public static class ClientCredentialsToken {
        @NonNull
        private final String token;
        @NonNull
        private final Date expiresIn;
        @NonNull
        private final List<String> scopes;
        @NonNull
        private final TokenType tokenType;
    }
}
