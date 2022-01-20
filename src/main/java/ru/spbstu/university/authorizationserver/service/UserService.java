package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.LoginInfo;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.UserInfo;
import ru.spbstu.university.authorizationserver.repository.UserRepository;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final PasswordEncryptionService passwordEncryptionService;
    @NonNull
    private final Generator<String> idGenerator;

    @NonNull
    public User create(@NonNull String login, @NonNull String password, @NonNull LoginInfo loginInfo, @NonNull UserInfo userInfo) {
        final User user = new User(idGenerator.generate(), login, passwordEncryptionService.encryptPassword(password),
                loginInfo, userInfo);

        return userRepository.save(user);
    }

    public Optional<User> get(@NonNull String id) {
        return userRepository.findById(id);
    }

    @NonNull
    public User getByLoginAndPassword(@NonNull String login, @NonNull String password) {
        return userRepository.getUserByLoginAndPassword(login, password).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public User getByLogin(@NonNull String login) {
        return userRepository.findById(login).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public User update(@NonNull String login, @NonNull String password) {
        final User userActual = getByLogin(login);
        userActual.setPassword(passwordEncryptionService.encryptPassword(password));

        return userRepository.save(userActual);
    }

    @NonNull
    public User addLoginInfo(@NonNull String login, @NonNull LoginInfo loginInfo) {
        final User user = getByLogin(login);
        user.setLoginInfo(loginInfo);

        return userRepository.save(user);
    }

    public void delete(@NonNull String login) {
        userRepository.deleteById(login);
    }
}
