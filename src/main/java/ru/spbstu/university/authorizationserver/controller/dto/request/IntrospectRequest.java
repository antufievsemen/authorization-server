package ru.spbstu.university.authorizationserver.controller.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class IntrospectRequest {
    @NonNull
    private final String token;
    @Nullable
    private final List<String> scopes;
}
