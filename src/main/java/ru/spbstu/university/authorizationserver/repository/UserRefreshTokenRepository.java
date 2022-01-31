package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.UserRefreshToken;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, String> {

    @NonNull
    Optional<UserRefreshToken> findByRefreshToken(@NonNull String token);

    @NonNull
    Optional<UserRefreshToken> findByUser_Sub(@NonNull String userId);
}
