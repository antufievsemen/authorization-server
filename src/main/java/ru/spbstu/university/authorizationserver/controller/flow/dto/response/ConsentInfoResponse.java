package ru.spbstu.university.authorizationserver.controller.flow.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.ConsentService;

@Getter
@AllArgsConstructor
public class ConsentInfoResponse {
    @NonNull
    private final String subject;
    @NonNull
    private final String requestedResponseType;
    @NonNull
    private final List<String> requestedScopes;
    @NonNull
    private final String requestedUrl;
    @NonNull
    private final ConsentService.ClientInfo clientInfo;
}
