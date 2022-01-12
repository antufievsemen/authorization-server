package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.LoginInfo;
import ru.spbstu.university.authorizationserver.repository.LoginInfoRepository;
import ru.spbstu.university.authorizationserver.service.exception.UserNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class LoginInfoService {

    @NonNull
    private final LoginInfoRepository loginInfoRepository;

    @NonNull
    public LoginInfo get(@NonNull String sub) {
        return loginInfoRepository.findById(sub).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public Optional<LoginInfo> getBySessionId(@NonNull String sessionId) {
        return loginInfoRepository.getLoginInfoBySessionId(sessionId);
    }

    @NonNull
    public LoginInfo updateSessionId(@NonNull String sub, @NonNull String sessionId) {
        final LoginInfo loginInfo = get(sub);
        loginInfo.setSessionId(sessionId);

        return loginInfoRepository.save(loginInfo);
    }
}
