package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.PkceParams;

public interface PkceParamsRepository {

    @NonNull
    PkceParams create(@NonNull String id, @NonNull PkceParams pkceParams);

    @NonNull
    Optional<PkceParams> get(@NonNull String id);

    void delete(@NonNull String id);
}
