package ru.spbstu.university.authorizationserver.controller.flow.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class AcceptConsentRequest {
    @NonNull
    private final List<String> grantedScopes;
    @NonNull
    private final List<String> grantedAccessTokenAudience;
}
