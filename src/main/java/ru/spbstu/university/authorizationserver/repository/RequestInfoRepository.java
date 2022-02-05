package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.RequestInfo;
import ru.spbstu.university.authorizationserver.model.RequestParams;

public interface RequestInfoRepository {
    @NonNull
    RequestInfo create(@NonNull String id, @NonNull RequestInfo RequestInfo);

    @NonNull
    Optional<RequestInfo> get(@NonNull String id);

    void delete(@NonNull String id);
}
