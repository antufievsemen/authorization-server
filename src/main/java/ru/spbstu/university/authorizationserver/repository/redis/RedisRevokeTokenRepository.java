package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.repository.RevokeTokenRepository;

@Repository
@AllArgsConstructor
public class RedisRevokeTokenRepository implements RevokeTokenRepository {
    @NonNull
    private final RedisTemplate<String, String> revokeTokenTemplate;

    @NonNull
    @Override
    public String save(@NonNull String id, @NonNull String token, @NonNull Date expireAt) {
        revokeTokenTemplate.opsForValue().set(id, token);
        revokeTokenTemplate.expireAt(id, expireAt);
        return token;
    }

    @NonNull
    @Override
    public Optional<String> get(@NonNull String id) {
        return Optional.ofNullable(revokeTokenTemplate.opsForValue().get(id));
    }
}
