package ru.spbstu.university.authorizationserver.service.auth.exception;

public class AccessTokenExpirationException extends RuntimeException {
    public AccessTokenExpirationException() {
        super("Access token expired");
    }
}
