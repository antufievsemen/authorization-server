package ru.spbstu.university.authorizationserver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum TokenEnum {
    ACCESS_TOKEN("access_token"),
    ID_TOKEN("id_token"),
    REFRESH_TOKEN("refresh_token");

    @NonNull
    private final String name;
}
