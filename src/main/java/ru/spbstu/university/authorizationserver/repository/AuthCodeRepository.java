package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;

public interface AuthCodeRepository {

    @NonNull
    String create(@NonNull String id, @NonNull String authCode);

    @NonNull
    Optional<String> get(@NonNull String authCode);

    void delete(@NonNull String id);
}
