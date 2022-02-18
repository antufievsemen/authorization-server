package ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception;

public class AccessTokenIsExpiredException extends Exception{
    public AccessTokenIsExpiredException() {
        super("Access token is expired");
    }
}
