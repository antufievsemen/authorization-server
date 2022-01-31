package ru.spbstu.university.authorizationserver.service.encyption.refresh;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.UserRefreshToken;
import ru.spbstu.university.authorizationserver.service.RefreshTokenService;
import ru.spbstu.university.authorizationserver.service.encyption.refresh.exception.RefreshTokenExpiredException;
import ru.spbstu.university.authorizationserver.service.encyption.refresh.exception.RefreshTokenNotValidException;


@Slf4j
@Service
public class RefreshTokenEncyptorService {
    @NonNull
    private final RefreshTokenService refreshTokenService;
    @NonNull
    private final Cipher cipher;
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_SUITE = "AES/CBC/NoPadding";
    private static final int AES_KEY_SIZE = 16;
    private static final int AES_BLOCK_SIZE = 16;
    private static final byte[] SECRET = "SECRET_123456789".getBytes(StandardCharsets.UTF_8);


    @SneakyThrows
    public RefreshTokenEncyptorService(@NonNull RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
        cipher = Cipher.getInstance(CIPHER_SUITE);
    }

    @SneakyThrows
    @NonNull
    public String create(@NonNull String userId) {
        byte[] iv = generate(AES_BLOCK_SIZE);
        byte[] salt = generate(AES_KEY_SIZE);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(salt, ALGORITHM), new IvParameterSpec(iv));
        final String token = Base64.getUrlEncoder().encodeToString(cipher.doFinal(SECRET));

        save(userId, token);

        return token;
    }

    private void save(@NonNull String userId, @NonNull String token) {
        refreshTokenService.create(userId, token);
    }

    @NonNull
    public UserRefreshToken validateRawToken(@NonNull String token) {
        final LocalDateTime now = LocalDateTime.now();
        final UserRefreshToken userRefreshToken = refreshTokenService.getByToken(token).orElseThrow(RefreshTokenNotValidException::new);
        final LocalDateTime expiredAt = userRefreshToken.getExpiredAt();
        if (expiredAt.isBefore(now)) {
            throw new RefreshTokenExpiredException();
        }

        return userRefreshToken;
    }

    public void revoke(@NonNull String token) {
        final UserRefreshToken userRefreshToken = refreshTokenService.getByToken(token).orElseThrow(RefreshTokenNotValidException::new);
        refreshTokenService.delete(userRefreshToken.getId());
    }

    private byte[] generate(int size) {
        byte[] random = new byte[size];
        (new SecureRandom()).nextBytes(random);
        return random;
    }
}