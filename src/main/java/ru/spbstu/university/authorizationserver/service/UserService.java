package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.repository.UserRepository;
import ru.spbstu.university.authorizationserver.repository.UserinfoRepository;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final UserinfoRepository userinfoRepository;

    @NonNull
    public User create(@NonNull String sub, @NonNull Client client, @NonNull List<Scope> scopes,
                       @NonNull String sessionId, @NonNull String state) {
        final User user = new User(sub, client, scopes, sessionId, state);
        return userRepository.save(user);
    }

    @NonNull
    public Optional<User> getBySessionId(@NonNull String sessionId) {
        return userRepository.getUserBySessionId(sessionId);
    }

    @NonNull
    public Optional<User> getByState(@NonNull String state) {
        return userRepository.getUserByState(state);
    }

    @NonNull
    public Optional<User> get(@NonNull String sub) {
        return userRepository.findById(sub);
    }

    public void delete(@NonNull String sub) {
        userRepository.deleteById(sub);
    }

    @NonNull
    public Optional<Map<String, String>> getUserInfo(@NonNull String subject) {
        return userinfoRepository.get(subject);
    }

    @NonNull
    public Map<String, String> createUserInfo(@NonNull String subject, @NonNull Map<String, String> userInfo) {
        userInfo.put("subject", subject);
        return userinfoRepository.create(subject, userInfo);
    }
}
