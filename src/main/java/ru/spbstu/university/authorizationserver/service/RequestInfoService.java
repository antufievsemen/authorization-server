package ru.spbstu.university.authorizationserver.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.RequestInfo;
import ru.spbstu.university.authorizationserver.repository.RequestInfoRepository;

@Service
@Transactional
@AllArgsConstructor
public class RequestInfoService {
    @NonNull
    private final RequestInfoRepository requestInfoRepository;

    @NonNull
    public RequestInfo create(@NonNull String id, @NonNull RequestInfo requestInfo) {
        return requestInfoRepository.create(id, requestInfo);
    }

    @NonNull
    public Optional<RequestInfo> get(@NonNull String challenge) {
        return requestInfoRepository.get(challenge);
    }

    public void delete(@NonNull String id) {
        requestInfoRepository.delete(id);
    }
}
