package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.ConsentParams;
import ru.spbstu.university.authorizationserver.repository.ConsentRequestParamsRepository;

@Repository
@AllArgsConstructor
public class RedisConsentRequestParamsRepository implements ConsentRequestParamsRepository {
    @NonNull
    private final RedisTemplate<String, ConsentParams> consentRequestParamsRedisTemplate;
    @NonNull
    @Override
    public ConsentParams creat(@NonNull String sessionId, @NonNull ConsentParams consentParams) {
        consentRequestParamsRedisTemplate.opsForValue().set(sessionId, consentParams);
        consentRequestParamsRedisTemplate.expire(sessionId, 10, TimeUnit.MINUTES);
        return consentParams;
    }

    @NonNull
    @Override
    public Optional<ConsentParams> get(@NonNull String sessionId) {
        return Optional.ofNullable(consentRequestParamsRedisTemplate.opsForValue().get(sessionId));
    }

    @Override
    public void delete(@NonNull String sessionId) {
        consentRequestParamsRedisTemplate.delete(sessionId);
    }
}
