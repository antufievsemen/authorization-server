package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.RequestParams;

public interface RequestParamsRepository {
    @NonNull
    RequestParams create(@NonNull String id, @NonNull RequestParams requestParams);

    @NonNull
    Optional<RequestParams> get(@NonNull String challenge);
}
