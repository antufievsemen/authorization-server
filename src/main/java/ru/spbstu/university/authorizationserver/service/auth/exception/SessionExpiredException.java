package ru.spbstu.university.authorizationserver.service.auth.exception;

public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException() {
        super("Authorization session is expired");
    }
}
