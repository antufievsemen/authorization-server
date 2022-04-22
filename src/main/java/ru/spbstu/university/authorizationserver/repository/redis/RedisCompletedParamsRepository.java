package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.cache.CompletedParams;
import ru.spbstu.university.authorizationserver.repository.CompletedParamsRepository;

@Repository
@AllArgsConstructor
public class RedisCompletedParamsRepository implements CompletedParamsRepository {
    @NonNull
    private final RedisTemplate<String, CompletedParams> redisTemplate;

    @NonNull
    @Override
    public CompletedParams create(@NonNull String id, @NonNull CompletedParams completedParams) {
        redisTemplate.opsForValue().set(id, completedParams);
        redisTemplate.expire(id, 10, TimeUnit.MINUTES);

        return completedParams;
    }

    @NonNull
    @Override
    public Optional<CompletedParams> get(@NonNull String id) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(id));
    }

    @Override
    public void delete(@NonNull String id) {
        redisTemplate.delete(id);
    }
}
