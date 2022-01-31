package ru.spbstu.university.authorizationserver.repository;


import java.util.Optional;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.PkceRequest;

public interface PkceRequestRepository {

    @NonNull
    PkceRequest create(@NonNull String id, @NonNull PkceRequest pkceRequest);

    @NonNull
    Optional<PkceRequest> get(@NonNull String id);
}
