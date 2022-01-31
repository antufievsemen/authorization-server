package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.repository.RequestParamsRepository;

@Service
@Transactional
@AllArgsConstructor
public class RequestParamsService {
    @NonNull
    private final RequestParamsRepository requestParamsRepository;

    @NonNull
    public RequestParams create(@NonNull RequestParams requestParams) {
        return requestParamsRepository.create(requestParams.getChallenge(), requestParams);
    }

    @NonNull
    public Optional<RequestParams> get(@NonNull String challenge) {
        return requestParamsRepository.get(challenge);
    }
}
