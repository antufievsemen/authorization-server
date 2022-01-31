package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.PkceRequest;
import ru.spbstu.university.authorizationserver.repository.PkceRequestRepository;

@Service
@Transactional
@AllArgsConstructor
public class PkceRequestService {
    @NonNull
    private final PkceRequestRepository pkceRequestRepository;

    @NonNull
    public PkceRequest create(@NonNull String id, @NonNull PkceRequest pkceRequest) {
        return pkceRequestRepository.create(id, pkceRequest);
    }

    @NonNull
    public Optional<PkceRequest> get(@NonNull String id) {
        return pkceRequestRepository.get(id);
    }
}
