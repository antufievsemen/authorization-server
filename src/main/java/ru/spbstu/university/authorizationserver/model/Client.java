package ru.spbstu.university.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class Client {
    @NonNull
    private final String id;
    @NonNull
    private final String clientSecret;
    @NonNull
    private final String grantTypeId;
    @NonNull
    private final String scopeId;
}
