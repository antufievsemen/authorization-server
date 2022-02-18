package ru.spbstu.university.authorizationserver.service.auth.dto.logout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.MultiValueMap;

@Getter
@AllArgsConstructor
public class LogoutAccept {
    @NonNull
    private final MultiValueMap<String, String> attributes;
}
