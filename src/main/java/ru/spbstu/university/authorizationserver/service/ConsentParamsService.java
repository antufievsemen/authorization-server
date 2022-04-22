package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.model.cache.ConsentParams;
import ru.spbstu.university.authorizationserver.repository.ConsentRequestParamsRepository;

@Service
@Transactional
@AllArgsConstructor
public class ConsentParamsService {
    @NonNull
    private final ConsentRequestParamsRepository consentRequestParamsRepository;

    @NonNull
    public ConsentParams create(@NonNull String id, @NonNull ConsentParams consentParams) {
        return consentRequestParamsRepository.creat(id, consentParams);
    }

    @NonNull
    public Optional<ConsentParams> get(@NonNull String id) {
        return consentRequestParamsRepository.get(id);
    }

    public void delete(@NonNull String id) {
        consentRequestParamsRepository.delete(id);
    }
}
