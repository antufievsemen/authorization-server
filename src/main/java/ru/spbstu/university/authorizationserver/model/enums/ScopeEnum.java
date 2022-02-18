package ru.spbstu.university.authorizationserver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum ScopeEnum {
    EMAIL("email"),
    OPENID("openid"),
    PROFILE("profile"),
    OFFLINE_ACCESS("offline_access");

    @NonNull
    private final String name;
}
