package ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class AcceptLoginRequest {
    @NonNull
    private final String sub;
}
