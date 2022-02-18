package ru.spbstu.university.authorizationserver.controller.auth.dto.request;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class AcceptConsentRequest {
    @NonNull
    private final List<String> permittedScopes;
    @NonNull
    private final List<String> permittedAudience;
    @Nullable
    private final Map<String, String> userinfo;
}
