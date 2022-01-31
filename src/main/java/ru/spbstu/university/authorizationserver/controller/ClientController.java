package ru.spbstu.university.authorizationserver.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.controller.dto.request.ClientRequest;
import ru.spbstu.university.authorizationserver.controller.dto.response.ClientResponse;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.ClientService;

@ServerName
@RestController
@AllArgsConstructor
public class ClientController {

    @NonNull
    private final ClientService clientService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients")
    public ClientResponse create(@NonNull @RequestBody ClientRequest request) {
        final List<ResponseTypeEnum> collect = request.getResponseTypes().stream().map(ResponseTypeEnum::valueOf).collect(Collectors.toList());
        return toResponse(clientService.create(request.getClientId(), request.getClientSecret(),
                request.getGrantTypes(), request.getScopes(), collect, request.getRedirectUri()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clients")
    public List<ClientResponse> getAll() {
        return clientService.getAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clients/{clientId}")
    public ClientResponse get(@NonNull @PathVariable String clientId) {
        return toResponse(clientService.getByClientId(clientId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/clients/{clientId}")
    public void delete(@NonNull @PathVariable String clientId) {
        clientService.delete(clientId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/clients/{clientId}")
    public ClientResponse update(@NonNull @RequestBody ClientRequest request,
                                 @NonNull @PathVariable String clientId) {
        final List<ResponseTypeEnum> collect = request.getResponseTypes().stream().map(ResponseTypeEnum::valueOf).collect(Collectors.toList());
        return toResponse(clientService.update(clientId, request.getClientId(), request.getClientSecret(), request.getGrantTypes(),
                request.getScopes(), collect, request.getRedirectUri()));
    }

    @NonNull
    private ClientResponse toResponse(@NonNull Client client) {
        return new ClientResponse(client.getClientId(), client.getClientSecret(), getGrantTypes(client.getGrantTypes()),
                getScopes(client.getScopes()), getResponseTypes(client.getResponseTypes()), client.getRedirectUri());
    }

    @NonNull
    private List<String> getScopes(@NonNull List<Scope> scopes) {
        return scopes.stream()
                .map(Scope::getName)
                .collect(Collectors.toList());
    }

    @NonNull
    private List<String> getResponseTypes(@NonNull List<ResponseType> scopes) {
        return scopes.stream()
                .map(responseType -> responseType.getResponseType().getName())
                .collect(Collectors.toList());
    }

    @NonNull
    private List<String> getGrantTypes(@NonNull List<GrantType> grantTypes) {
        return grantTypes.stream()
                .map(grantType -> grantType.getGrantType().getName())
                .collect(Collectors.toList());
    }

}
