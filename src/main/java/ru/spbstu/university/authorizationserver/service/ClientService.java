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
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.repository.ClientRepository;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotFoundException;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotUniqueException;
import ru.spbstu.university.authorizationserver.service.idgenerator.IdGenerator;

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
    public Client create(@NonNull String id, @NonNull String secret, @NonNull List<String> grantTypes,
                         @Nullable List<String> scopes, @Nullable String redirectUri) {
        final Optional<Client> client1 = clientRepository.findClientByClientId(id);

        if (client1.isPresent()) {
            throw new ClientNotUniqueException();
        }

        final Scope scope = new Scope(idGenerator.generate(), getScope(scopes));
        final List<GrantType> grantTypeList = grantTypeService.getByName(grantTypes);

        final Client client = new Client(idGenerator.generate(), id, secret, grantTypeList, scope, redirectUri);

        return clientRepository.save(client);
    }

    @NonNull
    public Client get(@NonNull String id) {
        return clientRepository.findClientByClientId(id).orElseThrow(ClientNotFoundException::new);
    }

    @NonNull
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @NonNull
    public Client update(@NonNull String clientId, @NonNull String requestClientId, @NonNull String secret, @NonNull List<String> grantTypes,
                         @Nullable List<String> scopes, @Nullable String redirectUri) {
        final List<GrantType> grantTypeList = grantTypeService.getByName(grantTypes);
        final Client clientActual = clientRepository.findClientByClientId(clientId).orElseThrow(ClientNotFoundException::new);
        final Scope scope = new Scope(clientActual.getScope().getId(), getScope(scopes));

        final Client client = new Client(clientActual.getId(), requestClientId, secret, grantTypeList, scope, redirectUri);

        return clientRepository.save(client);
    }

    public void delete(@NonNull String id) {
        clientRepository.deleteClientByClientId(id);
    }

    @Nullable
    private String getScope(@Nullable List<String> scopes) {
        return scopes != null ? scopes.stream()
                .reduce((s, s2) -> s.concat(";" + s2)).orElse(null) : null;
    }
}
