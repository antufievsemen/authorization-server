package ru.spbstu.university.authorizationserver.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.RefreshToken;
import ru.spbstu.university.authorizationserver.repository.RefreshTokenRepository;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;
import ru.spbstu.university.authorizationserver.service.exception.UserRefreshTokenNotFoundException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenService {
    @NonNull
    private final RefreshTokenRepository refreshTokenRepository;
    @NonNull
    private final UserService userService;
    @NonNull
    private final Generator<String> idGenerator;

    @NonNull
    public RefreshToken create(@NonNull String userId, @NonNull String token) {
        final User user = userService.get(userId).orElseThrow(UserNotFoundException::new);
        final LocalDateTime expiredDate = LocalDateTime.now().plusDays(30);
        final RefreshToken refreshToken = new RefreshToken(idGenerator.generate(), user, token, expiredDate);

        return refreshTokenRepository.save(refreshToken);
    }

    @NonNull
    public Optional<RefreshToken> getByToken(@NonNull String token) {
        return refreshTokenRepository.getRefreshTokenByToken(token);
    }

    @NonNull
    public Optional<RefreshToken> getByUserId(@NonNull String userId) {
        return refreshTokenRepository.getRefreshTokenByUser_Id(userId);
    }

    @NonNull
    public RefreshToken update(@NonNull String userId, @NonNull String token) {
        final RefreshToken refreshToken = getByUserId(userId).orElseThrow(UserRefreshTokenNotFoundException::new);
        final LocalDateTime expiredDate = LocalDateTime.now().plusDays(30);

        refreshToken.setToken(token);
        refreshToken.setExpiredAt(expiredDate);
        return refreshTokenRepository.save(refreshToken);
    }

    public void delete(@NonNull String id) {
        refreshTokenRepository.deleteById(id);
    }
}
