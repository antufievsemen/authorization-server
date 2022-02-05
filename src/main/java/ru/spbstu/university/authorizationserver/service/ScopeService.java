package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.repository.ClientRepository;
import ru.spbstu.university.authorizationserver.repository.ScopeRepository;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotFoundException;
import ru.spbstu.university.authorizationserver.service.exception.ScopeNotValidException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
@Transactional
@AllArgsConstructor
public class ScopeService {

    @NonNull
    private final ScopeRepository scopeRepository;
    @NonNull
    private final Generator<String> idGenerator;
    @NonNull
    private final ClientRepository clientRepository;

    @NonNull
    public List<Scope> update(@NonNull String clientId, @NonNull List<String> scopes) {
        final Client client = clientRepository.findByClientId(clientId).orElseThrow(ClientNotFoundException::new);

        final List<Scope> scopeList = create(scopes);
        client.setScopes(scopeList);
        clientRepository.save(client);

        return scopeList;
    }

    @NonNull
    public List<Scope> getAllByClientId(@NonNull String clientId) {
        return clientRepository.findByClientId(clientId).map(Client::getScopes)
                .orElseThrow(ClientNotFoundException::new);
    }

    @NonNull
    public List<Scope> create(@NonNull List<String> scopes) {
        final List<Scope> notExistScopes = getNotExistScopes(scopes);
        scopeRepository.saveAll(notExistScopes);

        return scopeRepository.getScopesByNameIn(scopes);
    }

    @NonNull
    public List<Scope> getAllByName(@NonNull List<String> scopes) {
        if (!scopeRepository.existsAllByNameIn(scopes)) {
            throw new ScopeNotValidException();
        }

        return scopeRepository.getScopesByNameIn(scopes);
    }

    @NonNull
    private List<Scope> getNotExistScopes(@NonNull List<String> scopes) {
        final List<Scope> listScopes = scopeRepository.getScopesByNameIn(scopes);
        final List<String> availableScopes = listScopes.stream().map(Scope::getName)
                .collect(Collectors.toList());

        return scopes.stream()
                .filter(s -> !availableScopes.contains(s))
                .map(s -> new Scope(idGenerator.generate(), s))
                .collect(Collectors.toList());
    }
}
