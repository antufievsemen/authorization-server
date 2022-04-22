package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.cache.ConsentParams;

public interface ConsentRequestParamsRepository {
    @NonNull
    ConsentParams creat(@NonNull String sessionId, @NonNull ConsentParams consentParams);

    @NonNull
    Optional<ConsentParams> get(@NonNull String sessionId);

    void delete(@NonNull String sessionId);
}
