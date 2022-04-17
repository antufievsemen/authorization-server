package ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.enums.RedirectResponseEnum;

@Getter
public class LoginAccept {
    @NonNull
    private final MultiValueMap<String, String> attributes;

    public LoginAccept(@NonNull String verifier, @NonNull String clientId, @NonNull String state,
                       @NonNull List<ResponseTypeEnum> responseTypes, @NonNull List<String> scopes,
                       @NonNull String redirectUri, @Nullable String nonce) {
        attributes = new LinkedMultiValueMap<>();
        attributes.add("login_verifier", verifier);
        attributes.add("client_id", clientId);
        attributes.add("state", state);
        attributes.put("responseTypes", responseTypes.stream().map(ResponseTypeEnum::getName).collect(Collectors.toList()));
        attributes.put("scopes", scopes);
        attributes.add("redirectUri", redirectUri);
        attributes.add("nonce", nonce);
    }
}
