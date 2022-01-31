package ru.spbstu.university.authorizationserver.service.flow.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum ResponseRedirect {
    CONSENT_CHALLENGE("consent_challenge"),
    LOGIN_CHALLENGE("login_challenge");

    @NonNull
    private final String name;
}
