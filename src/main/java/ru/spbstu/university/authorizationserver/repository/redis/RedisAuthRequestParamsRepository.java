package ru.spbstu.university.authorizationserver.repository.redis;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.AuthParams;
import ru.spbstu.university.authorizationserver.repository.AuthRequestParamsRepository;

@Repository
@AllArgsConstructor
public class RedisAuthRequestParamsRepository implements AuthRequestParamsRepository {
    @NonNull
    private final RedisTemplate<String, AuthParams> requestParamsRedisTemplate;

    @NonNull
    @Override
    public AuthParams create(@NonNull String sessionId, @NonNull AuthParams authParams) {
        requestParamsRedisTemplate.opsForValue().set(sessionId, authParams);
        requestParamsRedisTemplate.expireAt(sessionId, Instant.now().plus(3, ChronoUnit.MINUTES));
        return authParams;
    }

    @NonNull
    @Override
    public Optional<AuthParams> get(@NonNull String id) {
        return Optional.ofNullable(requestParamsRedisTemplate.opsForValue().get(id));
    }

    @Override
    public void delete(@NonNull String id) {
        requestParamsRedisTemplate.delete(id);
    }
}
