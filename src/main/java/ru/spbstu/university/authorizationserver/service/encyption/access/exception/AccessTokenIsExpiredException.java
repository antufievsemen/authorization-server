package ru.spbstu.university.authorizationserver.service.encyption.access.exception;

public class AccessTokenIsExpiredException extends RuntimeException{
    public AccessTokenIsExpiredException() {
        super("Access token is expired");
    }
}
