package ru.spbstu.university.authorizationserver.service.encyption.access.exception;

public class AccessTokenIsNotActiveException extends RuntimeException {
    public AccessTokenIsNotActiveException() {
        super("Access token is not active");
    }
}
