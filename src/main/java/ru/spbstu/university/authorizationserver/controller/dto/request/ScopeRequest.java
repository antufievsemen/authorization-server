package ru.spbstu.university.authorizationserver.controller.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScopeRequest {
    @NotBlank(message = "Scopes is required")
    private List<String> scopes;
}
