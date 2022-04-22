package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.cache.ConsentParams;
import ru.spbstu.university.authorizationserver.repository.ConsentRequestParamsRepository;

@Repository
@AllArgsConstructor
public class RedisConsentRequestParamsRepository implements ConsentRequestParamsRepository {
    @NonNull
    private final RedisTemplate<String, ConsentParams> redisTemplate;
    @NonNull
    @Override
    public ConsentParams creat(@NonNull String id, @NonNull ConsentParams consentParams) {
        redisTemplate.opsForValue().set(id, consentParams);
        redisTemplate.expire(id, 10, TimeUnit.MINUTES);
        return consentParams;
    }

    @NonNull
    @Override
    public Optional<ConsentParams> get(@NonNull String sessionId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(sessionId));
    }

    @Override
    public void delete(@NonNull String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
