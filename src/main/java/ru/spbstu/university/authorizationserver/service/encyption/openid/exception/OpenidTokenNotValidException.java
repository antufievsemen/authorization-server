package ru.spbstu.university.authorizationserver.service.encyption.openid.exception;

public class OpenidTokenNotValidException extends RuntimeException {
    public OpenidTokenNotValidException() {
        super("Open id token not valid");
    }
}
