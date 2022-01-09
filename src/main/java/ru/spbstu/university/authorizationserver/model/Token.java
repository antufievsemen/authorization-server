package ru.spbstu.university.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Token {
    @NonNull
    private final String token;

}
