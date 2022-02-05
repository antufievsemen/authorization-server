package ru.spbstu.university.authorizationserver.controller.flow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class AcceptLoginRequest {
    @NonNull
    private final String sub;
}
