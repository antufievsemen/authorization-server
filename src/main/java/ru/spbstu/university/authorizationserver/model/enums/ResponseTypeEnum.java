package ru.spbstu.university.authorizationserver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum ResponseTypeEnum {
    TOKEN("token"), CODE("code"), ID_TOKEN("id_token");

    @NonNull
    private final String name;
}
