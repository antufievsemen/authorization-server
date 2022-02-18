package ru.spbstu.university.authorizationserver.service;

import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.repository.AuthCodeRepository;
import ru.spbstu.university.authorizationserver.service.auth.exception.SessionExpiredException;

@Service
@Transactional
@AllArgsConstructor
public class AuthCodeService {
    @NonNull
    private final AuthCodeRepository authCodeRepository;

    @NonNull
    public String create(@NonNull String sessionId, @NonNull String code) {
        return authCodeRepository.create(sessionId, code);
    }

    @NonNull
    public String get(@NonNull String sessionId) {
        return authCodeRepository.get(sessionId).orElseThrow(SessionExpiredException::new);
    }

    public void delete(@NonNull String sessionId) {
        authCodeRepository.delete(sessionId);
    }
}
