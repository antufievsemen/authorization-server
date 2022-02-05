package ru.spbstu.university.authorizationserver.service.encyption.access;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ru.spbstu.university.authorizationserver.model.enums.TokenType;
import ru.spbstu.university.authorizationserver.service.encyption.access.exception.AccessTokenIsExpiredException;
import ru.spbstu.university.authorizationserver.service.encyption.access.exception.AccessTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Slf4j
@Service
public class AccessTokenEncryptorService {

    @NonNull
    private final Generator<String> idGenerator;
    private final int VALIDITY_TIME_IN_MS;
    private final KeyPair keyPair;
    @NonNull
    private final SelfIssuerSettings settings;


    @SneakyThrows
    @Autowired
    public AccessTokenEncryptorService(@NonNull Generator<String> idGenerator, @NonNull SelfIssuerSettings settings) {
        this.idGenerator = idGenerator;
        this.settings = settings;
        this.VALIDITY_TIME_IN_MS = 3_600_000;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    @SneakyThrows
    @NonNull
    public TokenInfo createJwt(@NonNull String sub, @NonNull String clientId,
                               @NonNull String nonce, @Nullable List<String> aud, @NonNull List<String> scopes) {
        final Map<String, Object> map = new HashMap<>();
        map.put("clientId", clientId);
        map.put("nonce", nonce);
        map.put("token_use", "access_token");
        map.put("scopes", scopes);
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
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();

        return new TokenInfo(sub, nonce, token, expiredTime, scopes, TokenType.JWT);
    }

    @SneakyThrows
    @NonNull
    public TokenInfo createJwt(@NonNull String accessToken) {
        final Claims claims = validate(accessToken);
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final String token = Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
        return new TokenInfo(claims.getSubject(), claims.get("nonce", String.class), token, expiredTime,
                claims.get("scopes", List.class), TokenType.JWT);
    }

    @NonNull
    public Claims validate(@NonNull String token) {
        Jws<Claims> claims = getClaims(token);
        final boolean isExpired = claims.getBody().getExpiration().after(new Date());
        if (isExpired) {
            throw new AccessTokenIsExpiredException();
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
    private Jws<Claims> getClaims(@NonNull String token) {
        Jws<Claims> claims;
        try {
            claims = Jwts.parser().setSigningKey(keyPair.getPrivate()).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AccessTokenNotValidException();
        }

        return claims;
    }

    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        @NonNull
        private final String nonce;
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
}
