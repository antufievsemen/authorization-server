package ru.spbstu.university.authorizationserver.service.auth.security.token.access.exception;

public class IncorrectAlgorithmException extends RuntimeException {
    public IncorrectAlgorithmException() {
        super("Incorrect algorithm");
    }
}
