package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Callback;
import ru.spbstu.university.authorizationserver.repository.CallbackRepository;
import ru.spbstu.university.authorizationserver.service.exception.CallbackNotValidException;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
@Transactional
@AllArgsConstructor
public class CallbackService {
    @NonNull
    private final CallbackRepository callbackRepository;
    @NonNull
    private final Generator<String> idGenerator;

    @NonNull
    public List<Callback> save(@NonNull List<String> callbacks) {
        final List<Callback> callbackList = callbacks.stream()
                .map(s -> new Callback(idGenerator.generate(), s))
                .collect(Collectors.toList());
        return callbackRepository.saveAll(callbackList);
    }

    public void delete(@NonNull List<Callback> callbacks) {
        callbackRepository.deleteAll(callbacks);
    }

    @NonNull
    public Optional<Callback> getByUrl(@NonNull String url) {
        return callbackRepository.getByUrl(url);
    }

    @NonNull
    public String validate(List<Callback> available, String requested) {
        return available.stream()
                .map(Callback::getUrl)
                .filter(s -> s.equals(requested))
                .findFirst()
                .orElseThrow(CallbackNotValidException::new);
    }
}
