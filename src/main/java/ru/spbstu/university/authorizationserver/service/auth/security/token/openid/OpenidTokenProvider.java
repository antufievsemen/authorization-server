package ru.spbstu.university.authorizationserver.service.auth.security.token.openid;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.model.Jwk;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.IntrospectBody;
import ru.spbstu.university.authorizationserver.service.auth.security.JwksService;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception.IncorrectAlgorithmException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception.OpenidTokenNotValidException;
import ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception.OpenidTokenWithNotExistUserException;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotFoundException;
import ru.spbstu.university.authorizationserver.service.exception.JwksNotFoundException;
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
        this.VALIDITY_TIME_IN_MS = 1000 * 60 * 60;
    }

    @NonNull
    public String create(@NonNull String sub, @Nullable String nonce, @NonNull String sessionId,
                         @NonNull String accessTokenHash, @Nullable Map<String, String> claims) {
        final User user = userService.get(sub).orElseThrow(OpenidTokenWithNotExistUserException::new);
        final Map<String, Object> map = new HashMap<>();
        Optional.ofNullable(claims).ifPresent(map::putAll);
        map.put("at_hash", accessTokenHash);
        Optional.ofNullable(nonce).ifPresent(s -> map.put("nonce", s));
        map.put("sid", sessionId);
        map.put("auth_time", LocalDateTime.now().getNano());
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final Keys keys = getOrCreateKeys(user.getClient().getClientId());
        return Jwts.builder()
                .setHeaderParam("kid", keys.getKid())
                .setClaims(map)
                .setSubject(sub)
                .setAudience("auth-code-client")
                .setIssuer(settings.getIssuer())
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keys.getPrivateKey())
                .compact();
    }

    @SneakyThrows
    @NonNull
    public IntrospectBody introspect(@NonNull String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        final JwtHeader jwtHeader = new ObjectMapper().readValue(header, JwtHeader.class);
        if (!jwtHeader.getAlg().equals("RS256")) {
            throw new IncorrectAlgorithmException();
        }
        final Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(getPrivateKey(jwtHeader.getKid()))
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            return new IntrospectBody(false, Jwts.parser().parseClaimsJws(chunks[1]).getBody());
        }
        return new IntrospectBody(true, claimsJws.getBody());
    }

    @SneakyThrows
    @NonNull
    private RSAPrivateKey getPrivateKey(@NonNull String kid) {
        final Jwk jwk = jwksService.get(kid).orElseThrow(JwksNotFoundException::new);
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(jwk.getPrivateKey());
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    @SneakyThrows
    @NonNull
    private Keys getOrCreateKeys(@NonNull String clientId) {
        final Jwk jwk = jwksService.getOrCreateJwk(clientId);
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(jwk.getPrivateKey());
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return new Keys(jwk.getKid(), (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec));
    }

    @Getter
    @AllArgsConstructor
    private static class Keys {
        @NonNull
        private final String kid;
        @NonNull
        private final RSAPrivateKey privateKey;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JwtHeader {
        @NonNull
        private String typ;
        @NonNull
        private String alg;
        @NonNull
        private String kid;
    }
}
