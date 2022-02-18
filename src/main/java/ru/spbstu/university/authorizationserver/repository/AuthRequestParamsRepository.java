package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.params.AuthParams;

public interface AuthRequestParamsRepository {
    @NonNull
    AuthParams create(@NonNull String id, @NonNull AuthParams authParams);

    @NonNull
    Optional<AuthParams> get(@NonNull String id);

    void delete(@NonNull String id);
}
