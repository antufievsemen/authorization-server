package ru.spbstu.university.authorizationserver.service.exception;

public class IncorrectClientCredentialsException extends RuntimeException {
    public IncorrectClientCredentialsException() {
        super("Incorrect client credentials");
    }
}
