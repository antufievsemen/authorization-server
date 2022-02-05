package ru.spbstu.university.authorizationserver.controller.flow.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.spbstu.university.authorizationserver.service.LoginService;

@Getter
@AllArgsConstructor
public class LoginInfoResponse {
    @NonNull
    private final String requestedResponseType;
    @NonNull
    private final List<String> requestedScopes;
    @NonNull
    private final String requestedUrl;
    @NonNull
    private final LoginService.ClientInfo clientInfo;
}
