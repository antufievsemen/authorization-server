package ru.spbstu.university.authorizationserver.repository.redis;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.PkceRequest;
import ru.spbstu.university.authorizationserver.repository.PkceRequestRepository;

@Repository
@AllArgsConstructor
public class RedisPkceRequestRepository implements PkceRequestRepository {
    @NonNull
    private final RedisTemplate<String, PkceRequest> pkceRequestRedisTemplate;

    @NonNull
    @Override
    public PkceRequest create(@NonNull String id, @NonNull PkceRequest pkceRequest) {
        pkceRequestRedisTemplate.opsForValue().set(id, pkceRequest);
        pkceRequestRedisTemplate.expireAt(id, Instant.now().plus(30, ChronoUnit.SECONDS));

        return pkceRequest;
    }

    @NonNull
    @Override
    public Optional<PkceRequest> get(@NonNull String id) {
        return Optional.ofNullable(pkceRequestRedisTemplate.opsForValue().get(id));
    }
}
