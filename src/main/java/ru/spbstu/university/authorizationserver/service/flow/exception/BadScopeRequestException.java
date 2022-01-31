package ru.spbstu.university.authorizationserver.service.flow.exception;

public class BadScopeRequestException extends RuntimeException {
    public BadScopeRequestException() {
        super("Client has`s right scopes");
    }
}
