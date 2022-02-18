package ru.spbstu.university.authorizationserver.service.auth.exception;

public class ConsentInfoExpiredException extends RuntimeException{
    public ConsentInfoExpiredException() {
        super("Consent info expired");
    }
}
