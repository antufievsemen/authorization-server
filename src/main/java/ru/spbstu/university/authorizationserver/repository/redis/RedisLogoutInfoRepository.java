package ru.spbstu.university.authorizationserver.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;
import ru.spbstu.university.authorizationserver.repository.LogoutInfoRepository;

@Repository
@AllArgsConstructor
public class RedisLogoutInfoRepository implements LogoutInfoRepository {
    @NonNull
    private final RedisTemplate<String, LogoutInfo> logoutInfoRedisTemplate;

    @NonNull
    @Override
    public LogoutInfo create(@NonNull String id, @NonNull LogoutInfo logoutInfo) {
        logoutInfoRedisTemplate.opsForValue().set(id, logoutInfo);
        logoutInfoRedisTemplate.expire(id, 3, TimeUnit.MINUTES);
        return logoutInfo;
    }

    @NonNull
    @Override
    public Optional<LogoutInfo> get(@NonNull String id) {
        return Optional.ofNullable(logoutInfoRedisTemplate.opsForValue().get(id));
    }

    @Override
    public void delete(@NonNull String id) {
        logoutInfoRedisTemplate.delete(id);
    }
}
