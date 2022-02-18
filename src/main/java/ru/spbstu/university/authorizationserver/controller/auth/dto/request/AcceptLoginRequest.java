package ru.spbstu.university.authorizationserver.controller.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AcceptLoginRequest {
    @NonNull
    private String subject;
}
