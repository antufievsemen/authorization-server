package ru.spbstu.university.authorizationserver.repository;

import java.util.Date;
import java.util.Optional;
import lombok.NonNull;

public interface AccessTokenRepository {
    void save(@NonNull String id, @NonNull String token, @NonNull Date expireAt);

    @NonNull
    Optional<String> get(@NonNull String id);
}
