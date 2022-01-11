package ru.spbstu.university.authorizationserver.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.repository.GrantTypeRepository;

@Service
@Transactional
@AllArgsConstructor
public class GrantTypeService {

    @NonNull
    private final GrantTypeRepository grantTypeRepository;

    @NonNull
    public List<GrantType> getByName(@NonNull List<String> grantTypeNames) {
        final List<GrantTypesEnum> grantTypesList = grantTypeNames.stream()
                .map(s -> GrantTypesEnum.valueOf(s.toUpperCase()))
                .collect(Collectors.toList());

        return grantTypeRepository.findAllByGrantTypeIn(grantTypesList);
    }
}
