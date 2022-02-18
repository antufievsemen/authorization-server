package ru.spbstu.university.authorizationserver.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @NonNull
    Optional<RefreshToken> getRefreshTokenByToken(@NonNull String token);

    @NonNull
    Optional<RefreshToken> getRefreshTokenByUser_Id(@NonNull String userId);
}
