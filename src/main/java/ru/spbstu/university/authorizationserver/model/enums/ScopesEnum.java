package ru.spbstu.university.authorizationserver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public enum ScopesEnum {
    OPEN_ID("open_id"),
    REFRESH_TOKEN("refresh_token");

    @NonNull
    private final String name;
}
