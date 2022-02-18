package ru.spbstu.university.authorizationserver.service.auth.exception;

public class RequestParamsNotValidException extends RuntimeException {
    public RequestParamsNotValidException() {
        super("Request params not valid");
    }
}
