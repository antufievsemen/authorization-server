package ru.spbstu.university.authorizationserver.service.auth.security.authcode;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.service.AuthCodeService;
import ru.spbstu.university.authorizationserver.service.auth.security.authcode.exception.AuthCodeIsNotValidException;

@Service
@Transactional
@AllArgsConstructor
public class AuthCodeProvider {
    @NonNull
    private final AuthCodeService authCodeService;

    @NonNull
    @SneakyThrows
    public String generate() {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final String encode = Base64.getEncoder().encodeToString(digest.digest(generate(16)));
        authCodeService.create(encode, encode);
        return encode;
    }

    public void validate(@NonNull String code) {
        authCodeService.get(code);
    }

    public void endAuthorization(@NonNull String code) {
        authCodeService.delete(code);
    }

    private byte[] generate(int size) {
        byte[] random = new byte[size];
        (new SecureRandom()).nextBytes(random);
        return random;
    }
}
