package ru.spbstu.university.authorizationserver.service.flow.exception;

public class ResponseTypeNotValidException extends RuntimeException {
    public ResponseTypeNotValidException() {
        super("Response type not valid to client");
    }
}
