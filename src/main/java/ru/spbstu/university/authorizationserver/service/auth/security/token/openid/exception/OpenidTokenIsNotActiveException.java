package ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception;

public class OpenidTokenIsNotActiveException extends Exception {
    public OpenidTokenIsNotActiveException() {
        super("Open id token is not active");
    }
}
