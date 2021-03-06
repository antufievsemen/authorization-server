package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @NonNull
    Optional<User> getUserBySessionId(@NonNull String sessionId);

    @NonNull
    Optional<User> getUserByState(@NonNull String sessionId);

    void deleteBySessionId(@NonNull String sessionId);
}
