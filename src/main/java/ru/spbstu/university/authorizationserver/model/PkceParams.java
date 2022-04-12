package ru.spbstu.university.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class PkceParams {
    @NonNull
    private final String codeChallenge;
    @NonNull
    private final String codeChallengeMethod;
}
