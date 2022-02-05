package ru.spbstu.university.authorizationserver.service;

import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.repository.RevokeTokenRepository;

@Service
@Transactional
@AllArgsConstructor
public class RevokeTokenService {
    @NonNull
    private final RevokeTokenRepository revokeTokenRepository;
    @NonNull
    public String save(@NonNull String id, @NonNull String token, @NonNull Date expireAt) {
        return revokeTokenRepository.save(id, token, expireAt);
    }

    @NonNull
    public Optional<String> get(@NonNull String id) {
        return revokeTokenRepository.get(id);
    }
}
