package ru.spbstu.university.authorizationserver.repository.redis;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.repository.RequestParamsRepository;

@Repository
@AllArgsConstructor
public class RedisRequestParamsRepository implements RequestParamsRepository {
    @NonNull
    private final RedisTemplate<String, RequestParams> requestParamsRedisTemplate;

    @NonNull
    @Override
    public RequestParams create(@NonNull String challenge, @NonNull RequestParams requestParams) {
        requestParamsRedisTemplate.opsForValue().set(challenge, requestParams);
        requestParamsRedisTemplate.expireAt(challenge, Instant.now().plus(1, ChronoUnit.MINUTES));
        return requestParams;
    }

    @NonNull
    @Override
    public Optional<RequestParams> get(@NonNull String challenge) {
        return Optional.ofNullable(requestParamsRedisTemplate.opsForValue().get(challenge));
    }
}
