package ru.spbstu.university.authorizationserver.service.token.openid.exception;

public class OpenidTokenIsNotActiveException extends RuntimeException {
    public OpenidTokenIsNotActiveException() {
        super("Open id token is not active");
    }
}
