package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.cache.CompletedParams;
import ru.spbstu.university.authorizationserver.repository.CompletedParamsRepository;

@Service
@Transactional
@AllArgsConstructor
public class CompletedParamsService {
    @NonNull
    private final CompletedParamsRepository completedParamsRepository;

    @NonNull
    public CompletedParams create(@NonNull String id, @NonNull CompletedParams completedParams) {
        return completedParamsRepository.create(id, completedParams);
    }

    @NonNull
    public Optional<CompletedParams> get(@NonNull String id) {
        return completedParamsRepository.get(id);
    }

    public void delete(@NonNull String id) {
        completedParamsRepository.delete(id);
    }
}
