package ru.spbstu.university.authorizationserver.controller.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    @NonNull
    private final Map<String, String> map;
}
