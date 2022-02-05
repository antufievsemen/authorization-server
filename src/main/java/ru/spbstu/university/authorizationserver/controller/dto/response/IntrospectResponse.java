package ru.spbstu.university.authorizationserver.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class IntrospectResponse {
    boolean active;
    @NonNull
    private final String clientId;
    @NonNull
    private final String subject;
    @NonNull
    private final List<String> scopes;
    @NonNull
    private final String tokenUse;
    @NonNull
    private final String aud;
    @NonNull
    private final String issuer;
    @NonNull
    private final String sid;
    private final long expireAt;
    private final long nbf;
}
