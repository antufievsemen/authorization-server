package ru.spbstu.university.authorizationserver.service.encyption.pkce;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.PkceRequest;
import ru.spbstu.university.authorizationserver.service.PkceRequestService;
import ru.spbstu.university.authorizationserver.service.encyption.pkce.exception.CodeChallengeMethodIncorrectException;
import ru.spbstu.university.authorizationserver.service.encyption.pkce.exception.PkceAuthIncorrectException;

@Service
@AllArgsConstructor
public class PkceValidatorEncryptorService {
    @NonNull
    private final PkceRequestService pkceRequestService;

    @NonNull
    public PkceRequest validate(@NonNull String code, @NonNull String state) {
        final PkceRequest pkceRequest = pkceRequestService.get(state).orElseThrow(PkceAuthIncorrectException::new);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(pkceRequest.getCodeChallengeMethod());
            String codeChallenge = Base64.getEncoder().encodeToString(messageDigest.digest(
                    code.getBytes(StandardCharsets.UTF_8)));
            if (pkceRequest.getCodeChallenge().equals(codeChallenge)) {
                throw new PkceAuthIncorrectException();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new CodeChallengeMethodIncorrectException();
        }
        return pkceRequest;
    }
}
