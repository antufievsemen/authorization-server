package ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Getter
public class ConsentAccept {
    @NonNull
    private final MultiValueMap<String, String> attributes;

    public ConsentAccept(@NonNull String completeVerifier, @NonNull String clientId, @NonNull String state,
                         @NonNull List<ResponseTypeEnum> responseTypes, @NonNull List<String> scopes,
                         @NonNull String redirectUri, @Nullable String nonce) {
        attributes = new LinkedMultiValueMap<>();
        attributes.add("consent_verifier", completeVerifier);
        attributes.add("client_id", clientId);
        attributes.put("response_types", responseTypes.stream().map(ResponseTypeEnum::getName).collect(Collectors.toList()));
        attributes.put("scopes", scopes);
        attributes.add("state", state);
        attributes.add("nonce", nonce);
        attributes.add("redirect_uri", redirectUri);
    }
}
