package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.repository.PkceParamsRepository;
import ru.spbstu.university.authorizationserver.model.PkceParams;

@Service
@Transactional
@AllArgsConstructor
public class PkceParamsService {
    @NonNull
    private final PkceParamsRepository pkceParamsRepository;

    @NonNull
    public PkceParams create(@NonNull String id, @NonNull PkceParams pkceParams) {
        return pkceParamsRepository.create(id, pkceParams);
    }

    @NonNull
    public Optional<PkceParams> get(@NonNull String id) {
        return pkceParamsRepository.get(id);
    }

    public void delete(@NonNull String id) {
        pkceParamsRepository.delete(id);
    }
}
