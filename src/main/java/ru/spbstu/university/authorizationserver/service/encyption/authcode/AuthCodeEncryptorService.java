package ru.spbstu.university.authorizationserver.service.encyption.authcode;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.service.AuthCodeService;
import ru.spbstu.university.authorizationserver.service.encyption.authcode.exception.AuthCodeIsNotValidException;

@Service
@AllArgsConstructor
public class AuthCodeEncryptorService {
    @NonNull
    public final AuthCodeService authCodeService;

    @NonNull
    @SneakyThrows
    public String generate() {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final String authCode = Base64.getEncoder().encodeToString(digest.digest(generate(16)));
        return authCodeService.create(authCode);
    }

    @NonNull
    public String validate(@NonNull String authCode) {
        return authCodeService.get(authCode).orElseThrow(AuthCodeIsNotValidException::new);
    }

    private byte[] generate(int size) {
        byte[] random = new byte[size];
        (new SecureRandom()).nextBytes(random);
        return random;
    }
}
