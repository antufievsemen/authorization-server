package ru.spbstu.university.authorizationserver.controller.crud.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class TokenResponse {
    @NonNull
    private final String token;
}
