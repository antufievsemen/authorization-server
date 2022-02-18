package ru.spbstu.university.authorizationserver.service.auth.security.token.openid.exception;

public class OpenidTokenWithNotExistUserException extends RuntimeException {
    public OpenidTokenWithNotExistUserException() {
        super("Open id is cannot be implemented to not exist user");
    }
}
