package ru.spbstu.university.authorizationserver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum TokenType {
    JWT("JWT"),
    BEARER("Bearer");

    @NonNull
    private final String type;
}
