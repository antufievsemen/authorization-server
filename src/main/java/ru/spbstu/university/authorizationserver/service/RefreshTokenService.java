package ru.spbstu.university.authorizationserver.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.UserRefreshToken;
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
    public UserRefreshToken create(@NonNull String userId, @NonNull String token) {
        final User user = userService.get(userId).orElseThrow(UserNotFoundException::new);
        final LocalDateTime expiredDate = LocalDateTime.now().plusDays(30);
        final UserRefreshToken userRefreshToken = new UserRefreshToken(idGenerator.generate(), user, token, expiredDate);

        return refreshTokenRepository.save(userRefreshToken);
    }

    @NonNull
    public Optional<UserRefreshToken> getByToken(@NonNull String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    @NonNull
    public Optional<UserRefreshToken> getByUserId(@NonNull String userId) {
        return refreshTokenRepository.findByUser_Sub(userId);
    }

    @NonNull
    public UserRefreshToken update(@NonNull String userId, @NonNull String token) {
        final UserRefreshToken userRefreshToken = getByUserId(userId).orElseThrow(UserRefreshTokenNotFoundException::new);
        final LocalDateTime expiredDate = LocalDateTime.now().plusDays(30);

        userRefreshToken.setRefreshToken(token);
        userRefreshToken.setExpiredAt(expiredDate);
        return refreshTokenRepository.save(userRefreshToken);
    }

    public void delete(@NonNull String id) {
        refreshTokenRepository.deleteById(id);
    }
}
