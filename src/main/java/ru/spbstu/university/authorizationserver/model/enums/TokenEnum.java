package ru.spbstu.university.authorizationserver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum TokenEnum {
    ACCESS_TOKEN("access_token"),
    OPENID_TOKEN("openid_token"),
    REFRESH_TOKEN("refresh_token");

    @NonNull
    private final String name;
}
