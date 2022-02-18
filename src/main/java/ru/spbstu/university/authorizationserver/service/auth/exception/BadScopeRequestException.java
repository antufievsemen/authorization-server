package ru.spbstu.university.authorizationserver.service.auth.exception;

public class BadScopeRequestException extends RuntimeException {
    public BadScopeRequestException() {
        super("Client has`s right scopes");
    }
}
