package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.repository.AuthCodeRepository;

@Service
@Transactional
@AllArgsConstructor
public class AuthCodeService {
    @NonNull
    private final AuthCodeRepository authCodeRepository;

    @NonNull
    public String create(@NonNull String authCode) {
        return authCodeRepository.create(authCode, authCode);
    }

    @NonNull
    public Optional<String> get(@NonNull String authCode) {
        return authCodeRepository.get(authCode);
    }

    public void delete(@NonNull String id) {
        authCodeRepository.delete(id);
    }
}
