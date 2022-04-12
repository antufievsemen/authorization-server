package ru.spbstu.university.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ConsentParams {
    @NonNull
    private AuthParams authParams;
    @NonNull
    private final User user;
}
