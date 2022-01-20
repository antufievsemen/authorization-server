package ru.spbstu.university.authorizationserver.service.token.access.exception;

public class AccessTokenIsNotActive extends RuntimeException {
    public AccessTokenIsNotActive() {
        super("Access token is not active");
    }
}
