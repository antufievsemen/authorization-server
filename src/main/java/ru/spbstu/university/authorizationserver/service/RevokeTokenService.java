package ru.spbstu.university.authorizationserver.service;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.repository.RevokeTokenRepository;

@Service
@Transactional
@AllArgsConstructor
public class RevokeTokenService {
    @NonNull
    private final RevokeTokenRepository revokeTokenRepository;

    @NonNull
    public String revoke(@NonNull String token, @NonNull Claims claims, @NonNull Date expireAt) {
        return revokeTokenRepository.save(token, claims.get("client_id", String.class), expireAt);
    }

    @NonNull
    public Optional<String> get(@NonNull String id) {
        return revokeTokenRepository.get(id);
    }
}
