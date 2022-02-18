package ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception;

public class OpenidTokenNotValidException extends Exception {
    public OpenidTokenNotValidException() {
        super("Open id token not valid");
    }
}
