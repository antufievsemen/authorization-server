package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.repository.GrantTypeRepository;
import ru.spbstu.university.authorizationserver.service.auth.exception.GrantTypeNotValidException;

@Service
@Transactional
@AllArgsConstructor
public class GrantTypeService {

    @NonNull
    private final GrantTypeRepository grantTypeRepository;

    @NonNull
    public List<GrantType> getByName(@NonNull List<GrantTypeEnum> grantTypeNames) {
        return grantTypeRepository.getGrantTypeByTypeIn(grantTypeNames);
    }

    @NonNull
    public List<GrantTypeEnum> validate(@NonNull List<GrantType> available, @NonNull List<GrantTypeEnum> requested) {
        final List<GrantTypeEnum> collect = available.stream()
                .map(GrantType::getType)
                .collect(Collectors.toList());
        if (!collect.containsAll(requested)) {
            throw new GrantTypeNotValidException();
        }

        return requested;
    }
}
