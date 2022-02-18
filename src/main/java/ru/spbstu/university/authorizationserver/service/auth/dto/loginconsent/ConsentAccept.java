package ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class ConsentAccept {
    @NonNull
    private final MultiValueMap<String, String> attributes;

    public ConsentAccept(@NonNull String consentVerifier) {
        attributes = new LinkedMultiValueMap<>();
        attributes.add("consent_verifier", consentVerifier);
    }
}
