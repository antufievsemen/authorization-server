package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.params.AuthParams;
import ru.spbstu.university.authorizationserver.repository.AuthRequestParamsRepository;

@Service
@Transactional
@AllArgsConstructor
public class RequestInfoService {
    @NonNull
    private final AuthRequestParamsRepository authRequestParamsRepository;

    @NonNull
    public AuthParams create(@NonNull String verifier, @NonNull AuthParams authParams) {
        return authRequestParamsRepository.create(verifier, authParams);
    }

    @NonNull
    public Optional<AuthParams> get(@NonNull String verifier) {
        return authRequestParamsRepository.get(verifier);
    }

    public void delete(@NonNull String verifier) {
        authRequestParamsRepository.delete(verifier);
    }
}
