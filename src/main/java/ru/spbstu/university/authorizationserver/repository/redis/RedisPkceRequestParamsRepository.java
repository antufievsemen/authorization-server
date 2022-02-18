package ru.spbstu.university.authorizationserver.repository.redis;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.params.PkceRequestParams;
import ru.spbstu.university.authorizationserver.repository.PkceRequestParamsRepository;

@Repository
@AllArgsConstructor
public class RedisPkceRequestParamsRepository implements PkceRequestParamsRepository {
    @NonNull
    private final RedisTemplate<String, PkceRequestParams> pkceRequestRedisTemplate;

    @NonNull
    @Override
    public PkceRequestParams create(@NonNull String id, @NonNull PkceRequestParams pkceRequestParams) {
        pkceRequestRedisTemplate.opsForValue().set(id, pkceRequestParams);
        pkceRequestRedisTemplate.expireAt(id, Instant.now().plus(3, ChronoUnit.MINUTES));

        return pkceRequestParams;
    }

    @NonNull
    @Override
    public Optional<PkceRequestParams> get(@NonNull String id) {
        return Optional.ofNullable(pkceRequestRedisTemplate.opsForValue().get(id));
    }

    @Override
    public void delete(@NonNull String id) {
        pkceRequestRedisTemplate.delete(id);
    }
}
