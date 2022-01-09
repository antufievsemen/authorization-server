package ru.spbstu.university.authorizationserver.controller.crud.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScopeRequest {
    @NonNull
    private List<String> scopes;
}
