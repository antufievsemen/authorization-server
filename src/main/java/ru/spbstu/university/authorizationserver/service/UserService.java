package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.LoginInfo;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.repository.UserRepository;
import ru.spbstu.university.authorizationserver.service.exception.UserConflictException;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    public User create(@NonNull String login, @NonNull String password, @NonNull String sessionId,
                       @NonNull String clientId, @NonNull List<String> scopes) {
        final Optional<User> user1 = userRepository.getUserByLoginInfo_SessionId(sessionId);
        if (user1.isPresent()) {
            throw new UserConflictException();
        }
        final LoginInfo loginInfo = new LoginInfo(sessionId, clientId, getScope(scopes));
        final User user = new User(login, password, loginInfo);

        return userRepository.save(user);
    }

    @NonNull
    public User getByLogin(@NonNull String login) {
        return userRepository.getUserByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public User getBySessionId(@NonNull String sessionId) {
        return userRepository.getUserByLoginInfo_SessionId(sessionId).orElseThrow(UserNotFoundException::new);
    }

    @Nullable
    private String getScope(@Nullable List<String> scopes) {
        return scopes != null ? scopes.stream()
                .reduce((s, s2) -> s.concat(";" + s2)).orElse(null) : null;
    }
}
