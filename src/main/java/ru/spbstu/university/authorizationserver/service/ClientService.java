package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.params.AuthClient;
import ru.spbstu.university.authorizationserver.model.Callback;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.repository.ClientRepository;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotFoundException;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotUniqueException;
import ru.spbstu.university.authorizationserver.service.exception.ClientCredentialsNotValidException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
@Transactional
@AllArgsConstructor
public class ClientService {
    @NonNull
    private final ClientRepository clientRepository;
    @NonNull
    private final Generator<String> idGenerator;
    @NonNull
    private final GrantTypeService grantTypeService;
    @NonNull
    private final ResponseTypeService responseTypeService;
    @NonNull
    private final CallbackService callbackService;
    @NonNull
    private final ScopeService scopeService;

    @NonNull
    public Client create(@NonNull String clientId, @NonNull String secret, @NonNull List<GrantTypeEnum> grantTypes,
                         @NonNull List<String> scopes, @NonNull List<ResponseTypeEnum> responseTypes, @NonNull List<String> callbacks) {
        final Optional<Client> notUniqueConstraint = clientRepository.findByClientId(clientId);

        if (notUniqueConstraint.isPresent()) {
            throw new ClientNotUniqueException();
        }

        final List<Scope> scopeList = scopeService.add(scopes);
        final List<GrantType> grantTypeList = grantTypeService.getByName(grantTypes);
        final List<ResponseType> responseTypeList = responseTypeService.getByNames(responseTypes);
        final List<Callback> callbackList = callbackService.save(callbacks);
        final Client client = new Client(idGenerator.generate(), clientId, secret, grantTypeList, scopeList,
                responseTypeList, callbackList);

        return clientRepository.save(client);
    }

    @NonNull
    public Client getByClientId(@NonNull String id) {
        return clientRepository.findByClientId(id).orElseThrow(ClientNotFoundException::new);
    }

    @NonNull
    public Client getByClientIdAndSecret(@NonNull String clientId, @NonNull String clientSecret) {
        return clientRepository.getClientByClientIdAndClientSecret(clientId, clientSecret)
                .orElseThrow(ClientCredentialsNotValidException::new);
    }

    @NonNull
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @NonNull
    public Client update(@NonNull String clientId, @NonNull String requestClientId, @NonNull String secret, @NonNull List<GrantTypeEnum> grantTypes,
                         @NonNull List<String> scopes, @NonNull List<ResponseTypeEnum> responseTypes, @NonNull List<String> callbacks) {
        final Client clientActual = clientRepository.findByClientId(clientId).orElseThrow(ClientNotFoundException::new);

        final List<Scope> scopeList = scopeService.add(scopes);
        final List<ResponseType> responseTypeList = responseTypeService.getByNames(responseTypes);
        final List<GrantType> grantTypeList = grantTypeService.getByName(grantTypes);

        callbackService.delete(clientActual.getCallbacks());
        final List<Callback> callbackList = callbackService.save(callbacks);

        final Client client = new Client(clientActual.getId(), requestClientId, secret, grantTypeList, scopeList,
                responseTypeList, callbackList);

        return clientRepository.save(client);
    }

    public void delete(@NonNull String id) {
        clientRepository.deleteClientByClientId(id);
    }

    @NonNull
    public AuthClient validate(@NonNull String clientId, @NonNull List<ResponseTypeEnum> responseTypes,
                               @NonNull List<GrantTypeEnum> grantTypes, @NonNull List<String> scopes,
                               @NonNull String callback) {
        final Client client = getByClientId(clientId);
        final List<ResponseTypeEnum> validResponseTypes = responseTypeService.validate(client.getResponseTypes(), responseTypes);
        final List<String> validScopes = scopeService.validate(client.getScopes(), scopes);
        final String validCallback = callbackService.validate(client.getCallbacks(), callback);

        return new AuthClient(clientId, grantTypes, validResponseTypes, validScopes, validCallback);
    }


}
