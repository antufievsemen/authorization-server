package ru.spbstu.university.authorizationserver.service.auth.dto.token;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class IntrospectBody {
    private final boolean active;
    @NonNull
    private final Claims claims;
}
