package ru.spbstu.university.authorizationserver.service.auth.dto.token;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
public class TokenResponseBody {
    @NonNull
    private final MultiValueMap<String, String> attributes;

    @NonNull
    @JsonAnyGetter
    public MultiValueMap<String, String> getAttributes() {
        return attributes;
    }
}
