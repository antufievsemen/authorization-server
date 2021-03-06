package ru.spbstu.university.authorizationserver.repository;

import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

public interface UserinfoRepository {
    @NonNull
    Map<String, String> create(@NonNull String subject, @NonNull Map<String, String> map);

    @NonNull
    Optional<Map<String, String>> get(@NonNull String subject);
}
