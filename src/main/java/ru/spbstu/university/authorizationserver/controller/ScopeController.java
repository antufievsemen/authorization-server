package ru.spbstu.university.authorizationserver.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.controller.dto.request.ScopeRequest;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.service.ScopeService;

@ServerName
@RestController
@AllArgsConstructor
public class ScopeController {

    @NonNull
    private final ScopeService scopeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clients/{clientId}/scopes")
    public List<String> getAll(@NonNull @PathVariable String clientId) {
        return toResponse(scopeService.getAllByClientId(clientId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/clients/{clientId}/scopes")
    public List<String> update(@NonNull @PathVariable String clientId, @NonNull @RequestBody ScopeRequest request) {
        return toResponse(scopeService.update(clientId, request.getScopes()));
    }

    @NonNull
    public List<String> toResponse(@NonNull List<Scope> scopes) {
        return scopes.stream()
                .map(Scope::getName)
                .collect(Collectors.toList());
    }
}
