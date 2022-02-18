package ru.spbstu.university.authorizationserver.service.auth.exception;

public class GrantTypeNotValidException extends RuntimeException{
    public GrantTypeNotValidException() {
        super("Grant types not allow this response type");
    }
}
