package ru.spbstu.university.authorizationserver.service.encyption.authcode;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthCodeEncryptorService {

    @NonNull
    @SneakyThrows
    public String generate() {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(digest.digest(generate(16)));
    }

    private byte[] generate(int size) {
        byte[] random = new byte[size];
        (new SecureRandom()).nextBytes(random);
        return random;
    }
}
