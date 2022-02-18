package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.repository.AuthCodeRepository;

@Repository
@AllArgsConstructor
public class RedisAuthCodeRepository implements AuthCodeRepository {
    @NonNull
    private final StringRedisTemplate stringRedisTemplate;

    @NonNull
    @Override
    public String create(@NonNull String sessionId, @NonNull String code) {
        stringRedisTemplate.opsForValue().set(sessionId, code);
        stringRedisTemplate.expire(sessionId, 3, TimeUnit.MINUTES);
        return code;
    }

    @NonNull
    @Override
    public Optional<String> get(@NonNull String sessionId) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(sessionId));
    }

    @Override
    public void delete(@NonNull String sessionId) {
        stringRedisTemplate.delete(sessionId);
    }
}
