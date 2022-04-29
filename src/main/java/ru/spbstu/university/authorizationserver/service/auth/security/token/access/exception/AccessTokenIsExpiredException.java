package ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception;

public class AccessTokenIsExpiredException extends RuntimeException{
    public AccessTokenIsExpiredException() {
        super("Access token is expired");
    }
}
