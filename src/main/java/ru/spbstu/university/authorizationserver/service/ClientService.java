package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.repository.ClientRepository;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotFoundException;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotUniqueException;
import ru.spbstu.university.authorizationserver.service.generator.impl.IdGenerator;

@Service
@Transactional
@AllArgsConstructor
public class ClientService {
    @NonNull
    private final ClientRepository clientRepository;
    @NonNull
    private final IdGenerator idGenerator;
    @NonNull
    private final GrantTypeService grantTypeService;
    @NonNull
    private final ResponseTypeService responseTypeService;
    @NonNull
    private final ScopeService scopeService;

    @NonNull
    public Client create(@NonNull String id, @NonNull String secret, @NonNull List<String> grantTypes,
                         @NonNull List<String> scopes, @NonNull List<ResponseTypeEnum> responseTypes, @Nullable String redirectUri) {
        final Optional<Client> client1 = clientRepository.findByClientId(id);

        if (client1.isPresent()) {
            throw new ClientNotUniqueException();
        }

        final List<Scope> scopeList = scopeService.create(scopes);
        final List<GrantType> grantTypeList = grantTypeService.getByName(grantTypes);
        final List<ResponseType> responseTypeList = responseTypeService.getByNames(responseTypes);

        final Client client = new Client(idGenerator.generate(), id, secret, grantTypeList, scopeList,
                responseTypeList, redirectUri);

        return clientRepository.save(client);
    }

    @NonNull
    public Client getByClientId(@NonNull String id) {
        return clientRepository.findByClientId(id).orElseThrow(ClientNotFoundException::new);
    }

    @NonNull
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @NonNull
    public Client update(@NonNull String clientId, @NonNull String requestClientId, @NonNull String secret, @NonNull List<String> grantTypes,
                         @NonNull List<String> scopes, @NonNull List<ResponseTypeEnum> responseTypes, @Nullable String redirectUri) {
        final List<GrantType> grantTypeList = grantTypeService.getByName(grantTypes);
        final Client clientActual = clientRepository.findByClientId(clientId).orElseThrow(ClientNotFoundException::new);
        final List<Scope> scopeList = scopeService.create(scopes);
        final List<ResponseType> responseTypeList = responseTypeService.getByNames(responseTypes);

        final Client client = new Client(clientActual.getId(), requestClientId, secret, grantTypeList, scopeList,
                responseTypeList, redirectUri);

        return clientRepository.save(client);
    }

    public void delete(@NonNull String id) {
        clientRepository.deleteClientByClientId(id);
    }
}
