package ru.spbstu.university.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.service.flow.dto.enums.ResponseRedirect;

@Getter
@AllArgsConstructor
public class RequestInfo {
    @Nullable
    private final String subject;
    @NonNull
    private final RequestParams requestParams;
    @NonNull
    private final ResponseRedirect responseRedirect;
}
