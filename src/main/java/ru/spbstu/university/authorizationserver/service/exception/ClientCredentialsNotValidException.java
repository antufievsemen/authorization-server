package ru.spbstu.university.authorizationserver.service.exception;

public class ClientCredentialsNotValidException extends RuntimeException {
    public ClientCredentialsNotValidException() {
        super("Incorrect client credentials");
    }
}
