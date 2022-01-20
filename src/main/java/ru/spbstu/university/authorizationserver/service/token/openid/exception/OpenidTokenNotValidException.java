package ru.spbstu.university.authorizationserver.service.token.openid.exception;

public class OpenidTokenNotValidException extends RuntimeException {
    public OpenidTokenNotValidException() {
        super("Open id token not valid");
    }
}
