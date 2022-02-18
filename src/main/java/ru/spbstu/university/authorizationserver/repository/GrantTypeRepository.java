package ru.spbstu.university.authorizationserver.repository;

import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;

@Repository
public interface GrantTypeRepository extends JpaRepository<GrantType, String> {
    @NonNull
    List<GrantType> getGrantTypeByTypeIn(@NonNull List<GrantTypeEnum> grantedTypes);
}
