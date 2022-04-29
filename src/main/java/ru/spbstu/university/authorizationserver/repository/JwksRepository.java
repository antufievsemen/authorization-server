package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.Jwk;
import ru.spbstu.university.authorizationserver.model.enums.TokenEnum;

@Repository
public interface JwksRepository extends JpaRepository<Jwk, String> {
    @NonNull
    Optional<Jwk> findByKid(@NonNull String kid);
    @NonNull
    Optional<Jwk> findByClientId(@NonNull String clientId);
}
