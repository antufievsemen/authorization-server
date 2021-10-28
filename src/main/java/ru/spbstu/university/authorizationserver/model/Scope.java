package ru.spbstu.university.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.model.enums.ScopesEnum;

@AllArgsConstructor
@Getter
public class Scope {
    @NonNull
    private final String id;
    @NonNull
    private final ScopesEnum scope;
}
