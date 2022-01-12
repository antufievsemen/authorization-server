package ru.spbstu.university.authorizationserver.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.LoginInfo;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.UserInfo;
import ru.spbstu.university.authorizationserver.repository.UserRepository;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;
import ru.spbstu.university.authorizationserver.service.idgenerator.Generator;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final PasswordEncryptionService passwordEncryptionService;
    @NonNull
    private final Generator idGenerator;

    @NonNull
    public User create(@NonNull String login, @NonNull String password, @NonNull LoginInfo loginInfo, @NonNull UserInfo userInfo) {
        final User user = new User(idGenerator.generate(), login, passwordEncryptionService.encryptPassword(password),
                loginInfo, userInfo);

        return userRepository.save(user);
    }

    @NonNull
    public User get(@NonNull String login, @NonNull String password) {
        return userRepository.getUserByLoginAndPassword(login, password).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public User get(@NonNull String login) {
        return userRepository.findById(login).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public User update(@NonNull String login, @NonNull String password) {
        final User userActual = get(login);
        userActual.setPassword(passwordEncryptionService.encryptPassword(password));

        return userRepository.save(userActual);
    }

    @NonNull
    public User addLoginInfo(@NonNull String login, @NonNull LoginInfo loginInfo) {
        final User user = get(login);
        user.setLoginInfo(loginInfo);

        return userRepository.save(user);
    }

    public void delete(@NonNull String login) {
        userRepository.deleteById(login);
    }
}
