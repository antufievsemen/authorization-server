package ru.spbstu.university.authorizationserver.service.auth.security.token.openid;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception.OpenidTokenIsNotActiveException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception.OpenidTokenWithNotExistUserException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception.AccessTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception.OpenidTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
public class OpenidTokenProvider {
    @NonNull
    private final UserService userService;
    @NonNull
    private final Generator<String> idGenerator;
    private final long VALIDITY_TIME_IN_MS;
    private final KeyPair keyPair;
    @NonNull
    private final SelfIssuerSettings settings;

    @SneakyThrows
    @Autowired
    public OpenidTokenProvider(@NonNull UserService userService, @NonNull Generator<String> idGenerator, @NonNull SelfIssuerSettings settings) {
        this.userService = userService;
        this.idGenerator = idGenerator;
        this.settings = settings;
        this.VALIDITY_TIME_IN_MS = 1000 * 60 * 10;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    @NonNull
    public String create(@NonNull String sub, @NonNull String nonce, @NonNull String sessionId,
                         @NonNull String accessTokenHash, @NonNull LocalDateTime authAt, @NonNull Map<String, String> claims) {
        userService.get(sub).orElseThrow(OpenidTokenWithNotExistUserException::new);
        final Map<String, Object> map = new HashMap<>(claims);
        map.put("at_hash", accessTokenHash);
        map.put("nonce", nonce);
        map.put("sid", sessionId);
        map.put("auth_time", authAt.getNano());
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
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }

    @NonNull
    public Claims validate(@NonNull String token, @NonNull String accessTokenHash) throws OpenidTokenIsNotActiveException, OpenidTokenNotValidException {
        Jws<Claims> claims = getClaims(token);
        final boolean isExpired = claims.getBody().getExpiration().after(new Date());
        final String at_hash = (String) claims.getBody().get("at_hash");
        if (!at_hash.equals(accessTokenHash)) {
            throw new OpenidTokenNotValidException();
        } else if (isExpired) {
            throw new OpenidTokenIsNotActiveException();
        }

        return claims.getBody();
    }

    @NonNull
    public Claims validate(@NonNull String token) throws OpenidTokenIsNotActiveException {
        Jws<Claims> claims = getClaims(token);
        final boolean isExpired = claims.getBody().getExpiration().after(new Date());
        if (isExpired) {
            throw new OpenidTokenIsNotActiveException();
        }

        return claims.getBody();
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
