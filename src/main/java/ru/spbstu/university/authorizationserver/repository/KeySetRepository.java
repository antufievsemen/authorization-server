package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.KeySet;
import ru.spbstu.university.authorizationserver.model.enums.TokenEnum;

@Repository
public interface KeySetRepository extends JpaRepository<KeySet, String> {

    @NonNull
    Optional<KeySet> findByClientId(@NonNull String clientId);

    @NonNull
    Optional<KeySet> findByClientIdAndTokenType(@NonNull String clientId, @NonNull TokenEnum tokenType);
}
