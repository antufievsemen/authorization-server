package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.CompletedParams;

public interface CompletedParamsRepository {
    @NonNull
    CompletedParams create(@NonNull String id, @NonNull CompletedParams completedParams);

    @NonNull
    Optional<CompletedParams> get(@NonNull String id);

    void delete(@NonNull String id);
}
