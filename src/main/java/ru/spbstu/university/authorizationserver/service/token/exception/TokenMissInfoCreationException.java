package ru.spbstu.university.authorizationserver.service.token.exception;

public class TokenMissInfoCreationException extends RuntimeException {
    public TokenMissInfoCreationException() {
        super("Refresh token or code is required");
    }
}
