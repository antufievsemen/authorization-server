package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.params.ConsentParams;
import ru.spbstu.university.authorizationserver.repository.ConsentRequestParamsRepository;

@Service
@Transactional
@AllArgsConstructor
public class ConsentParamsService {
    @NonNull
    private final ConsentRequestParamsRepository consentRequestParamsRepository;

    @NonNull
    public ConsentParams create(@NonNull String verifier, @NonNull ConsentParams consentParams) {
        return consentRequestParamsRepository.creat(verifier, consentParams);
    }

    @NonNull
    public Optional<ConsentParams> get(@NonNull String verifier) {
        return consentRequestParamsRepository.get(verifier);
    }

    public void delete(@NonNull String verifier) {
        consentRequestParamsRepository.delete(verifier);
    }
}
