package ru.spbstu.university.authorizationserver.service.auth.dto.logout;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.model.User;

@Getter
public class LogoutAccept {
    @NonNull
    private final MultiValueMap<String, String> attributes;

    public LogoutAccept(@NonNull String redirectUri, @Nullable String state, @NonNull String idTokenHint,
                        @NonNull String logoutVerifier) {
        attributes = new LinkedMultiValueMap<>();
        attributes.add("id_token_hint", idTokenHint);
        attributes.add("post_logout_redirect_uri", redirectUri);
        attributes.add("logout_verifier", logoutVerifier);

        if (Objects.nonNull(state)) {
            attributes.add("state", state);
        }
    }
}
