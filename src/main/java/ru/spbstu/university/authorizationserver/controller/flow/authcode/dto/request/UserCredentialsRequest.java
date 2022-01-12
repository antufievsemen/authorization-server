package ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentialsRequest {
    @NotBlank(message = "Login is required")
    private final String login;
    @NotBlank(message = "Login is required")
    private final String password;
}
