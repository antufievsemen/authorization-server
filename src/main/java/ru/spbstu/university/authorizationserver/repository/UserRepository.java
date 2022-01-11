package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @NonNull
    Optional<User> getUserByLoginAndPassword(@NonNull String login, @NonNull String password);
}
