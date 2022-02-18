package ru.spbstu.university.authorizationserver.service.auth.security.codeverifier;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.service.generator.exception.CodeVerifierNotValidException;

@Service
public class CodeVerifierProvider {
    @NonNull
    private final Cipher coder;
    private final Cipher decoder;

    @SneakyThrows
    public CodeVerifierProvider() {
        SecretKey secretKey = new SecretKeySpec(generate(16), "AES");

        this.coder = Cipher.getInstance("AES/ECB/PKCS5Padding");
        coder.init(Cipher.ENCRYPT_MODE, secretKey);

        this.decoder = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decoder.init(Cipher.DECRYPT_MODE, secretKey);
    }

    public void validate(@NonNull String codeVerifier) {
        try {
            decoder.doFinal(Base64.getUrlDecoder().decode(codeVerifier));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new CodeVerifierNotValidException();
        }
    }

    @SneakyThrows
    @NonNull
    public String generate() {
        return Base64.getUrlEncoder().encodeToString(coder.doFinal(generate(16)));
    }

    private byte[] generate(int size) {
        byte[] random = new byte[size];
        (new SecureRandom()).nextBytes(random);
        return random;
    }
}
