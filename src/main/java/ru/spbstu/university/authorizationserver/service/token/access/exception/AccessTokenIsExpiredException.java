package ru.spbstu.university.authorizationserver.service.token.access.exception;

public class AccessTokenIsExpiredException extends RuntimeException{
    public AccessTokenIsExpiredException() {
        super("Access token is expired");
    }
}
