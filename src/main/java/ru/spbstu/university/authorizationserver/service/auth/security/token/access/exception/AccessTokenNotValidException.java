package ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception;

public class AccessTokenNotValidException extends RuntimeException{
    public AccessTokenNotValidException() {
        super("Access token not valid");
    }
}
