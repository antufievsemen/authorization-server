package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.repository.UserRepository;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    public User create(@NonNull String sub, @NonNull Client client, @NonNull List<Scope> scopes,
                       @NonNull String sessionId) {
        final User user = new User(sub, client, scopes, sessionId);
        return userRepository.save(user);
    }

    @NonNull
    public Optional<User> getBySessionId(@NonNull String sessionId) {
        return userRepository.getUserBySessionId(sessionId);
    }

    @NonNull
    public Optional<User> get(@NonNull String sub) {
        return userRepository.findById(sub);
    }

    public void delete(@NonNull String sub) {
        userRepository.deleteById(sub);
    }

    public void deleteBySessionId(@NonNull String sessionId) {
        userRepository.deleteBySessionId(sessionId);
    }

    @NonNull
    public User update(@NonNull String sub, @NonNull String sessionId, boolean consented) {
        final User user = get(sub).orElseThrow(UserNotFoundException::new);
        user.setSessionId(sessionId);

        return userRepository.save(user);
    }
}
