package ru.spbstu.university.authorizationserver.service.token.openid;

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
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.UserInfo;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.generator.Generator;
import ru.spbstu.university.authorizationserver.service.token.access.AccessTokenService;
import ru.spbstu.university.authorizationserver.service.token.access.exception.AccessTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.token.openid.exception.OpenidTokenIsNotActiveException;
import ru.spbstu.university.authorizationserver.service.token.openid.exception.OpenidTokenNotValidException;

@Service
public class OpenidTokenService {
    @NonNull
    private final UserService userService;
    @NonNull
    private final AccessTokenService accessTokenService;
    @NonNull
    private final Generator<String> idGenerator;
    private final long VALIDITY_TIME_IN_MS;
    private final String ISSUER;
    private final KeyPair keyPair;

    @SneakyThrows
    @Autowired
    public OpenidTokenService(@NonNull UserService userService, @NonNull AccessTokenService accessTokenService, @NonNull Generator<String> idGenerator) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
        this.idGenerator = idGenerator;
        this.ISSUER = "http://auth-server/";
        this.VALIDITY_TIME_IN_MS = 1728000000;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    @NonNull
    public String create(@NonNull String userId, @NonNull String sub, @NonNull String clientId, @NonNull String nonce,
                         @NonNull String sessionId, @NonNull String accessTokenHash,
                         @NonNull LocalDateTime authAt) {
        final User user = userService.get(userId);
        final Map<String, Object> map = new HashMap<>();

        setUserInfo(map, user.getUserInfo());
        map.put("clientId", clientId);
        map.put("at_hash", accessTokenHash);
        map.put("nonce", nonce);
        map.put("sid", sessionId);
        map.put("auth_time", authAt.getNano());
        map.put("token_type", "Bearer");
        map.put("token_use", "access_token");
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        return Jwts.builder()
                .setClaims(map)
                .setSubject(sub)
                .setAudience("auth-code-client")
                .setIssuer(ISSUER)
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }

    @NonNull
    public Claims validate(@NonNull String token, @NonNull String accessTokenHash) {
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

    private void setUserInfo(@NonNull Map<String, Object> map, @NonNull UserInfo userInfo) {
        map.put("email", userInfo.getEmail());
        map.put("email_verified", userInfo.isEmailVerified());
        map.put("firstname", userInfo.getFirstname());
        map.put("lastname", userInfo.getLastname());
        map.put("gender", userInfo.getGender());
        map.put("phone", userInfo.getPhoneNumber());
        map.put("phone_verified", userInfo.isPhoneVerified());
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
