package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.LogoutInfo;
import ru.spbstu.university.authorizationserver.repository.redis.RedisLogoutInfoRepository;

@Service
@Transactional
@AllArgsConstructor
public class LogoutInfoService {
    @NonNull
    private final RedisLogoutInfoRepository redisLogoutInfoRepository;

    @NonNull
    public LogoutInfo create(@NonNull String id, @NonNull LogoutInfo logoutInfo) {
        return redisLogoutInfoRepository.create(id, logoutInfo);
    }

    @NonNull
    public Optional<LogoutInfo> get(@NonNull String id) {
        return redisLogoutInfoRepository.get(id);
    }

    public void delete(@NonNull String id) {
        redisLogoutInfoRepository.delete(id);
    }
}
