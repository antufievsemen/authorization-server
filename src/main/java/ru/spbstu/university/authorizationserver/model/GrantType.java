package ru.spbstu.university.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;

@AllArgsConstructor
@Getter
public class GrantType {
    @NonNull
    private final String id;
    @NonNull
    private final GrantTypesEnum grantType;
}
