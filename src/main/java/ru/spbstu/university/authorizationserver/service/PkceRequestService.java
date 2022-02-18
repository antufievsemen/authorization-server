package ru.spbstu.university.authorizationserver.service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.params.PkceRequestParams;
import ru.spbstu.university.authorizationserver.repository.PkceRequestParamsRepository;
import ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception.CodeChallengeMethodIncorrectException;
import ru.spbstu.university.authorizationserver.service.exception.CodeChallengeSizeIncorrectException;

@Service
@Transactional
@AllArgsConstructor
public class PkceRequestService {
    @NonNull
    private final PkceRequestParamsRepository pkceRequestParamsRepository;
    private static final String CODE_CHALLENGE_METHOD = "RSA";


    @NonNull
    public PkceRequestParams create(@NonNull String id, @NonNull PkceRequestParams pkceRequestParams) {
        return pkceRequestParamsRepository.create(id, pkceRequestParams);
    }

    @NonNull
    public Optional<PkceRequestParams> get(@NonNull String id) {
        return pkceRequestParamsRepository.get(id);
    }

    @NonNull
    public PkceRequestParams validate(@NonNull String codeChallenge, @NonNull String method) {
        if (!CODE_CHALLENGE_METHOD.equals(method)) {
            throw new CodeChallengeMethodIncorrectException();
        }
        final byte[] bytes = codeChallenge.getBytes(StandardCharsets.UTF_8);
        if (bytes.length != 16) {
            throw new CodeChallengeSizeIncorrectException();
        }

        return new PkceRequestParams(codeChallenge, method);
    }
}
