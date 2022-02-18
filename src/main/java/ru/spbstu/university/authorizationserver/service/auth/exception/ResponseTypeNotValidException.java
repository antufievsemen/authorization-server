package ru.spbstu.university.authorizationserver.service.auth.exception;

public class ResponseTypeNotValidException extends RuntimeException {
    public ResponseTypeNotValidException() {
        super("Response type not valid to client");
    }
}
