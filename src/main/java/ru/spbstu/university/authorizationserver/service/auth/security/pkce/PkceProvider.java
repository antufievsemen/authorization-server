package ru.spbstu.university.authorizationserver.service.auth.security.pkce;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.cache.PkceParams;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception.CodeChallengeMethodIncorrectException;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception.PkceAuthIncorrectException;

@Service
@AllArgsConstructor
public class PkceProvider {

    public void validate(@NonNull String code, @NonNull PkceParams pkceParams) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(pkceParams.getCodeChallengeMethod());
            String codeChallenge = Base64.getEncoder().encodeToString(messageDigest.digest(
                    code.getBytes(StandardCharsets.UTF_8)));
            if (pkceParams.getCodeChallenge().equals(codeChallenge)) {
                throw new PkceAuthIncorrectException();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new CodeChallengeMethodIncorrectException();
        }
    }
}
