package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.repository.UserinfoRepository;

@Repository
@AllArgsConstructor
public class RedisUserInfoRepository implements UserinfoRepository {

    @NonNull
    private final RedisTemplate<String, Map<String, String>> redisTemplate;

    @NonNull
    @Override
    public Map<String, String> create(@NonNull String subject, @NonNull Map<String, String> map) {
        redisTemplate.opsForValue().set(subject, map);
        redisTemplate.expire(subject, 5, TimeUnit.MINUTES);
        return map;
    }

    @NonNull
    @Override
    public Optional<Map<String, String>> get(@NonNull String subject) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(subject));
    }
}
