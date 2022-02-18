package ru.spbstu.university.authorizationserver.service.auth.dto.redirect.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum RedirectResponseEnum {
    CONSENT_VERIFIER("consent_verifier"),
    LOGIN_VERIFIER("login_verifier"),
    LOGOUT_VERIFIER("logout_verifier");

    @NonNull
    private final String name;
}
