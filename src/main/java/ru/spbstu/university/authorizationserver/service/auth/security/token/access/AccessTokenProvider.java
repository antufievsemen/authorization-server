package ru.spbstu.university.authorizationserver.service.auth.security.token.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.model.Jwk;
import ru.spbstu.university.authorizationserver.model.enums.TokenType;
import ru.spbstu.university.authorizationserver.service.auth.dto.token.IntrospectBody;
import ru.spbstu.university.authorizationserver.service.auth.security.JwksService;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception.IncorrectAlgorithmException;
import ru.spbstu.university.authorizationserver.service.exception.JwksNotFoundException;
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
        this.VALIDITY_TIME_IN_MS = 1000 * 5 * 60;
    }

    @SneakyThrows
    @NonNull
    public TokenInfo createJwt(@NonNull String sub, @NonNull String clientId, @NonNull String sessionId,
                               @Nullable List<String> aud, @NonNull List<String> scopes) {
        final Map<String, Object> map = new HashMap<>();
        map.put("client_id", clientId);
        map.put("token_use", "access_token");
        map.put("scope", scopes);
        map.put("sid", sessionId);
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final Keys keys = getOrCreateKeys(clientId);
        final String token = Jwts.builder()
                .setHeaderParam("kid", keys.getKid())
                .setClaims(map)
                .setSubject(sub)
                .setAudience(aud != null ? aud.toString() : "")
                .setIssuer(settings.getIssuer())
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keys.getPrivateKey())
                .compact();

        return new TokenInfo(sub, token, expiredTime, scopes, TokenType.JWT);
    }

    @SneakyThrows
    @NonNull
    public ClientCredentialsToken createJwt(@NonNull String clientId, @NonNull String sessionId,
                                            @NonNull List<String> scopes) {
        final Map<String, Object> map = new HashMap<>();
        map.put("client_id", clientId);
        map.put("token_use", "access_token");
        map.put("scope", scopes);
        map.put("sid", sessionId);
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final Keys keys = getOrCreateKeys(clientId);
        final String token = Jwts.builder()
                .setHeaderParam("kid", keys.getKid())
                .setClaims(map)
                .setIssuer(settings.getIssuer())
                .setId(idGenerator.generate())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setNotBefore(now)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keys.getPrivateKey())
                .compact();

        return new ClientCredentialsToken(token, expiredTime, scopes, TokenType.JWT);
    }

    @SneakyThrows
    @NonNull
    public TokenInfo createJwt(@NonNull Claims claims, @NonNull String clientId) {
        final Date now = new Date();
        final Date expiredTime = new Date(now.getTime() + VALIDITY_TIME_IN_MS);
        final Keys keys = getOrCreateKeys(clientId);
        final String token = Jwts.builder()
                .setHeaderParam("kid", keys.getKid())
                .setClaims(claims)
                .setExpiration(expiredTime)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, keys.getPrivateKey())
                .compact();

        return new TokenInfo(claims.getSubject(), token, expiredTime, claims.get("scopes", List.class),
                TokenType.JWT);
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
    public String getHash(@NonNull String token) {
        final byte[] bytes = DigestUtils.sha256(token);
        final byte[] hashBytes = new byte[16];
        System.arraycopy(bytes, 0, hashBytes, 0, 15);

        return Base64.getEncoder().encodeToString(hashBytes);
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
