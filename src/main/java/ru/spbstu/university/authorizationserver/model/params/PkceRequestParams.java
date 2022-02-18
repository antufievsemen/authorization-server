package ru.spbstu.university.authorizationserver.model.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class PkceRequestParams {
    @NonNull
    private final String codeChallenge;
    @NonNull
    private final String codeChallengeMethod;
}
