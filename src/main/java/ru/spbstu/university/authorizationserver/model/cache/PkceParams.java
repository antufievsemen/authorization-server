package ru.spbstu.university.authorizationserver.model.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PkceParams {
    @NonNull
    private String codeChallenge;
    @NonNull
    private String codeChallengeMethod;
}
