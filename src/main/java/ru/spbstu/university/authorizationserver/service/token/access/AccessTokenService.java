package ru.spbstu.university.authorizationserver.service.token.access;

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
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.repository.AccessTokenRepository;
import ru.spbstu.university.authorizationserver.service.generator.Generator;
import ru.spbstu.university.authorizationserver.service.token.access.exception.AccessTokenIsExpiredException;
import ru.spbstu.university.authorizationserver.service.token.access.exception.AccessTokenIsNotActive;
import ru.spbstu.university.authorizationserver.service.token.access.exception.AccessTokenNotValidException;

@Slf4j
@Service
public class AccessTokenService {

    @NonNull
    private final Generator<String> idGenerator;
    @NonNull
    private final AccessTokenRepository accessTokenRepository;
    private final String ISSUER;
    private final int VALIDITY_TIME_IN_MS;
    private final KeyPair keyPair;


    @SneakyThrows
    @Autowired
    public AccessTokenService(@NonNull Generator<String> idGenerator, @NonNull AccessTokenRepository accessTokenRepository) {
        this.idGenerator = idGenerator;
        this.accessTokenRepository = accessTokenRepository;
        this.ISSUER = "http://auth-server/";
        this.VALIDITY_TIME_IN_MS = 3_600_000;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    @SneakyThrows
    @NonNull
    public String createJwt(@NonNull String sub, @NonNull String clientId,
                            @NonNull String nonce, @Nullable String aud) {
        final Map<String, Object> map = new HashMap<>();
        map.put("clientId", clientId);
        map.put("nonce", nonce);
        map.put("token_type", "Bearer");
        map.put("token_use", "access_token");
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        return Jwts.builder()
                .setClaims(map)
                .setSubject(sub)
                .setAudience(aud)
                .setIssuer(ISSUER)
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }

    public Claims validate(@NonNull String token) {
        Jws<Claims> claims = getClaims(token);
        final Optional<String> optionalAccessToken = accessTokenRepository.get(claims.getBody().getId());
        final boolean isExpired = claims.getBody().getExpiration().after(new Date());

        if (optionalAccessToken.isPresent()) {
            throw new AccessTokenIsNotActive();
        } else if (isExpired) {
            throw new AccessTokenIsExpiredException();
        }

        return claims.getBody();
    }

    public void revoke(@NonNull String token) {
        Jws<Claims> claims = getClaims(token);
        final String id = claims.getBody().getId();
        accessTokenRepository.get(id).ifPresentOrElse(s -> {
                    throw new AccessTokenIsNotActive();
                },
                () -> accessTokenRepository.save(id, token, claims.getBody().getExpiration()));
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
}
