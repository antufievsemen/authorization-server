package ru.spbstu.university.authorizationserver.service.auth.exception;

public class PostTokenBadRequest extends RuntimeException {
    public PostTokenBadRequest() {
        super("Request body has incorrect or miss matched info");
    }
}
