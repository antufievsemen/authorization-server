package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.authcode.exception;

public class AuthCodeValidationException extends RuntimeException {
    public AuthCodeValidationException() {
        super("Auth code not valid");
    }
}
