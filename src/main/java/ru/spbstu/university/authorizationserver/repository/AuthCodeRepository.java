package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;

public interface AuthCodeRepository {
    @NonNull
    String create(@NonNull String sessionId, @NonNull String code);

    @NonNull
    Optional<String> get(@NonNull String sessionId);

    void delete(@NonNull String sessionId);
}
