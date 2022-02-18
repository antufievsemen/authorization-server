package ru.spbstu.university.authorizationserver.controller.dto.response;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@AllArgsConstructor
public class IntrospectResponse {
    boolean active;
    @NonNull
    private final Map<String, Object> claims;
}
