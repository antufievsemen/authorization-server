package ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception;

public class AccessTokenIsNotActiveException extends RuntimeException {
    public AccessTokenIsNotActiveException() {
        super("Access token is not active");
    }
}
