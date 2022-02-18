package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.repository.ResponseTypeRepository;
import ru.spbstu.university.authorizationserver.service.auth.exception.ResponseTypeNotValidException;

@Service
@Transactional
@AllArgsConstructor
public class ResponseTypeService {
    @NonNull
    private final ResponseTypeRepository responseTypeRepository;

    @NonNull
    public List<ResponseType> getByNames(@NonNull List<ResponseTypeEnum> responseTypes) {
        return responseTypeRepository.getResponseTypeByTypeIn(responseTypes);
    }

    @NonNull
    List<ResponseTypeEnum> validate(@NonNull List<ResponseType> available, @NonNull List<ResponseTypeEnum> requested) {
        final List<ResponseTypeEnum> collect = available.stream()
                .map(ResponseType::getType)
                .collect(Collectors.toList());
        if (!collect.containsAll(requested)) {
            throw new ResponseTypeNotValidException();
        }

        return requested;
    }
}
