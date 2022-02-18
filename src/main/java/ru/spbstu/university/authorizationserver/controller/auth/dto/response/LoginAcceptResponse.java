package ru.spbstu.university.authorizationserver.controller.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class LoginAcceptResponse {
    @NonNull
    private final String redirectTo;
}
