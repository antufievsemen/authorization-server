package ru.spbstu.university.authorizationserver.repository.redis;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.RequestInfo;
import ru.spbstu.university.authorizationserver.repository.RequestInfoRepository;

@Repository
@AllArgsConstructor
public class RedisRequestInfoRepository implements RequestInfoRepository {
    @NonNull
    private final RedisTemplate<String, RequestInfo> requestParamsRedisTemplate;

    @NonNull
    @Override
    public RequestInfo create(@NonNull String challenge, @NonNull RequestInfo requestInfo) {
        requestParamsRedisTemplate.opsForValue().set(challenge, requestInfo);
        requestParamsRedisTemplate.expireAt(challenge, Instant.now().plus(1, ChronoUnit.MINUTES));
        return requestInfo;
    }

    @NonNull
    @Override
    public Optional<RequestInfo> get(@NonNull String id) {
        return Optional.ofNullable(requestParamsRedisTemplate.opsForValue().get(id));
    }

    @Override
    public void delete(@NonNull String id) {
        requestParamsRedisTemplate.delete(id);
    }
}
