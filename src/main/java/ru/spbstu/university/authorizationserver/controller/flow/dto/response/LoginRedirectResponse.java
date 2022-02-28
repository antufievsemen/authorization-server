package ru.spbstu.university.authorizationserver.controller.flow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class LoginRedirectResponse {
    @NonNull
    private final String redirectTo;
}
