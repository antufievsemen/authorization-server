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
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.dto.request.ClientRequest;
import ru.spbstu.university.authorizationserver.controller.dto.response.ClientResponse;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.service.ClientService;

import static ru.spbstu.university.authorizationserver.util.CollectionConverter.callbacksGetUrls;
import static ru.spbstu.university.authorizationserver.util.CollectionConverter.grantTypesGetNames;
import static ru.spbstu.university.authorizationserver.util.CollectionConverter.responseTypesGetNames;
import static ru.spbstu.university.authorizationserver.util.CollectionConverter.scopesGetNames;

@ApiV1
@RestController
@AllArgsConstructor
public class ClientController {

    @NonNull
    private final ClientService clientService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients")
    public ClientResponse create(@NonNull @RequestBody ClientRequest request) {
        return toResponse(clientService.create(request.getClientId(), request.getClientSecret(),
                request.getGrantTypes(), request.getScopes(), request.getResponseTypes(), request.getCallbacks()));
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
        return toResponse(clientService.update(clientId, request.getClientId(), request.getClientSecret(), request.getGrantTypes(),
                request.getScopes(), request.getResponseTypes(), request.getCallbacks()));
    }

    @NonNull
    private ClientResponse toResponse(@NonNull Client client) {
        return new ClientResponse(client.getClientId(), client.getClientSecret(), grantTypesGetNames(client.getGrantTypes()),
                scopesGetNames(client.getScopes()), responseTypesGetNames(client.getResponseTypes()), callbacksGetUrls(client.getCallbacks()));
    }
}
