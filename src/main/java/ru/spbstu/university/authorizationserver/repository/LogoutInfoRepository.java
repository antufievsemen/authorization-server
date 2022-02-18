package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;

public interface LogoutInfoRepository {

    @NonNull
    LogoutInfo create(@NonNull String id, @NonNull LogoutInfo infoLogoutInfo);

    @NonNull
    Optional<LogoutInfo> get(@NonNull String id);

    void delete(@NonNull String id);
}
