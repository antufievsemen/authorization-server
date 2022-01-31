package ru.spbstu.university.authorizationserver.service.exception;

public class ScopeNotValidException extends RuntimeException {
    public ScopeNotValidException() {
        super("Request has invalid scope");
    }
}
