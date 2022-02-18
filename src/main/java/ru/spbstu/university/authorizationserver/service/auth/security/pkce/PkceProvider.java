package ru.spbstu.university.authorizationserver.service.auth.security.pkce;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.params.AuthParams;
import ru.spbstu.university.authorizationserver.model.params.PkceRequestParams;
import ru.spbstu.university.authorizationserver.service.PkceRequestService;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception.PkceAuthIncorrectException;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception.CodeChallengeMethodIncorrectException;

@Service
@AllArgsConstructor
public class PkceProvider {

    public void validate(@NonNull String code, @NonNull AuthParams.PkceParams pkceParams) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(pkceParams.getMethod());
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
