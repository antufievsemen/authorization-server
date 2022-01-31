package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.repository.ResponseTypeRepository;

@Service
@Transactional
@AllArgsConstructor
public class ResponseTypeService {
    @NonNull
    private final ResponseTypeRepository responseTypeRepository;

    @NonNull
    public List<ResponseType> getByNames(@NonNull List<ResponseTypeEnum> responseTypes) {
        return responseTypeRepository.findAllByResponseTypeIn(responseTypes);
    }
}
