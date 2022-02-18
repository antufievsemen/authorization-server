package ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.enums.RedirectResponseEnum;

@Getter
public class LoginAccept {
    @NonNull
    private final MultiValueMap<String, String> attributes;

    public LoginAccept(@NonNull String verifier) {
        attributes = new LinkedMultiValueMap<>();
        attributes.add("login_verifier", verifier);
    }
}
