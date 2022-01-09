package ru.spbstu.university.authorizationserver.service.idgenerator;

import lombok.NonNull;

public interface Generator {

    @NonNull
    String generate();
}
