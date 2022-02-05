package ru.spbstu.university.authorizationserver.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class RevokeRequest {
    @NonNull
    private final String token;
}
