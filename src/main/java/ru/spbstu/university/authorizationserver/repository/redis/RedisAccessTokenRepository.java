package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.repository.AccessTokenRepository;

@Repository
@AllArgsConstructor
public class RedisAccessTokenRepository implements AccessTokenRepository {
    @NonNull
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(@NonNull String id, @NonNull String token, @NonNull Date expireAt) {
        redisTemplate.opsForValue().set(id, token);
        redisTemplate.expireAt(id, expireAt);
    }

    @NonNull
    @Override
    public Optional<String> get(@NonNull String id) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(id));
    }
}
