package ru.spbstu.university.authorizationserver.repository.redis;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.repository.RevokeTokenRepository;

@Repository
@AllArgsConstructor
public class RedisRevokeTokenRepository implements RevokeTokenRepository {
    @NonNull
    private final StringRedisTemplate stringRedisTemplate;

    @NonNull
    @Override
    public String save(@NonNull String id, @NonNull String clientId, @NonNull Date expireAt) {
        stringRedisTemplate.opsForValue().set(id, clientId);
        stringRedisTemplate.expireAt(id, expireAt);
        return clientId;
    }

    @NonNull
    @Override
    public Optional<String> get(@NonNull String id) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(id));
    }
}
