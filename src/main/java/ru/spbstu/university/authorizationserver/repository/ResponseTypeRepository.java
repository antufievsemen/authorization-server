package ru.spbstu.university.authorizationserver.repository;

import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Repository
public interface ResponseTypeRepository extends JpaRepository<ResponseType, String> {
    @NonNull
    List<ResponseType> findAllByNameIn(@NonNull List<ResponseTypeEnum> responseTypes);
}
