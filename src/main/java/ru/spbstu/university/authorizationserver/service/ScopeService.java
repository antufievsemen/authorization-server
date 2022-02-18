package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.repository.ScopeRepository;
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
    public List<Scope> add(@NonNull List<String> scopes) {
        final List<Scope> notExistScopes = getNotExistScopes(scopes);
        scopeRepository.saveAll(notExistScopes);

        return scopeRepository.getScopesByNameIn(scopes);
    }

    @NonNull
    public List<Scope> getAllByName(@NonNull List<String> scopes) {
        if (!scopeRepository.existsScopesByNameIn(scopes)) {
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

    @NonNull
    public List<String> validate(@NonNull List<Scope> available, @NonNull List<String> requested) {
        final List<String> collect = available.stream()
                .map(Scope::getName)
                .collect(Collectors.toList());
        if (!collect.containsAll(requested)) {
            throw new ScopeNotValidException();
        }

        return requested;
    }
}
