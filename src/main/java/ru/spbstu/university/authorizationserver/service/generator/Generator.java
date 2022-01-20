package ru.spbstu.university.authorizationserver.service.generator;

import lombok.NonNull;

public interface Generator<T> {

    @NonNull
    T generate();
}
