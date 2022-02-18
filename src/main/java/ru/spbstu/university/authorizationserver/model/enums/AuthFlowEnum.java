package ru.spbstu.university.authorizationserver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum AuthFlowEnum {
    AUTHORIZATION_FLOW("authorization_flow"),
    OPENID_FLOW("openid_flow"),
    PKCE_FLOW("pkce_flow"),
    IMPLICIT_FLOW("implicit_flow");

    @NonNull
    private final String name;
}
