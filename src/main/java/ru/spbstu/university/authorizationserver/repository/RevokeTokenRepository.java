package ru.spbstu.university.authorizationserver.repository;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.Optional;
import lombok.NonNull;

public interface RevokeTokenRepository {
    String save(@NonNull String id, @NonNull String clientId, @NonNull Date expireAt);

    @NonNull
    Optional<String> get(@NonNull String id);
}
