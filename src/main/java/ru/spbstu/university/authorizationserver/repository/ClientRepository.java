package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    @NonNull
    Optional<Client> findByClientId(@NonNull String clientId);

    void deleteClientByClientId(@NonNull String clientId);

    @NonNull
    Optional<Client> getClientByClientIdAndClientSecret(@NonNull String clientId, @NonNull String clientSecret);
}
