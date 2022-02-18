package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.Callback;

@Repository
public interface CallbackRepository extends JpaRepository<Callback, String> {
    @NonNull
    Optional<Callback> getByUrl(@NonNull String url);

    @NonNull
    Optional<Callback> getCallbackByUrlEquals(@NonNull String url);
}
