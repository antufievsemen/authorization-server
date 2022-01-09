package ru.spbstu.university.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.university.authorizationserver.model.Scope;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, String> {
}
