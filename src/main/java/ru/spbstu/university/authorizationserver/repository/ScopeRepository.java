package ru.spbstu.university.authorizationserver.repository;

import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.Scope;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, String> {
    @NonNull
    List<Scope> getScopesByNameIn(@NonNull List<String> scopeNames);
    boolean existsAllByNameIn(@NonNull List<String> scopeNames);
}
