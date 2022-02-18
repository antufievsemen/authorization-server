package ru.spbstu.university.authorizationserver.repository;


import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.params.PkceRequestParams;

public interface PkceRequestParamsRepository {

    @NonNull
    PkceRequestParams create(@NonNull String id, @NonNull PkceRequestParams pkceRequestParams);

    @NonNull
    Optional<PkceRequestParams> get(@NonNull String id);

    void delete(@NonNull String id);
}
