package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.PkceParams;
import ru.spbstu.university.authorizationserver.repository.PkceParamsRepository;

@Repository
@AllArgsConstructor
public class RedisPkceParamsRepository implements PkceParamsRepository {
    @NonNull
    private final RedisTemplate<String, PkceParams> redisTemplate;

    @NonNull
    @Override
    public PkceParams create(@NonNull String id, @NonNull PkceParams pkceParams) {
        redisTemplate.opsForValue().set(id, pkceParams);
        redisTemplate.expire(id, 3, TimeUnit.MINUTES);
        return pkceParams;
    }

    @NonNull
    @Override
    public Optional<PkceParams> get(@NonNull String id) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(id));
    }

    @Override
    public void delete(@NonNull String id) {
        redisTemplate.delete(id);
    }
}
