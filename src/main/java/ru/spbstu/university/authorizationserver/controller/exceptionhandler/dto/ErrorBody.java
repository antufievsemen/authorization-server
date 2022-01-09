package ru.spbstu.university.authorizationserver.controller.exceptionhandler.dto;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorBody {
    private final int status;
    @NonNull
    private final String code;
    @NonNull
    private final String message;

    public ErrorBody(@NonNull HttpStatus status, @NonNull String message) {
        this.status = status.value();
        this.code = status.name();
        this.message = message;
    }
}
