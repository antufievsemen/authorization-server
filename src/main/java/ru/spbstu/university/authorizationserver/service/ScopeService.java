package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Client;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.repository.ScopeRepository;

@Service
@Transactional
@AllArgsConstructor
public class ScopeService {

    @NonNull
    private final ScopeRepository scopeRepository;
    @NonNull
    private final ClientService clientService;

    public List<String> update(@NonNull String clientId, @Nullable List<String> scopes) {
        final Client client = clientService.get(clientId);
        final Scope resultScope = new Scope(client.getScope().getId(), getScope(scopes));

        scopeRepository.save(resultScope);
        return scopes;
    }

    @NonNull
    public Optional<String> getAllByClientId(@NonNull String clientId) {
        return Optional.ofNullable(clientService.get(clientId).getScope().getScope());
    }

    @Nullable
    private String getScope(@Nullable List<String> scopes) {
        return scopes != null ? scopes.stream()
                .reduce((s, s2) -> s.concat(";" + s2)).orElse(null) : null;
    }
}
