package ru.spbstu.university.authorizationserver.repository.redis;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.repository.AuthCodeRepository;

@Repository
@AllArgsConstructor
public class RedisAuthCodeRepository implements AuthCodeRepository {
    @NonNull
    private final RedisTemplate<String, String> authCodeTemplate;

    @NonNull
    @Override
    public String create(@NonNull String id, @NonNull String authCode) {
        authCodeTemplate.opsForValue().set(id, authCode);
        authCodeTemplate.expireAt(id, Instant.now().plus(1, ChronoUnit.MINUTES));
        return authCode;
    }

    @NonNull
    @Override
    public Optional<String> get(@NonNull String authCode) {
        return Optional.ofNullable(authCodeTemplate.opsForValue().get(authCode));
    }

    @Override
    public void delete(@NonNull String id) {
        authCodeTemplate.delete(id);
    }
}
