package ru.spbstu.university.authorizationserver.service.auth.dto.logout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.User;

@Getter
@AllArgsConstructor
public class LogoutInfo {
    @NonNull
    private final User user;
    @Nullable
    private final String postRedirectUri;
}
