package ru.spbstu.university.authorizationserver.service.token.refresh.exception;

public class RefreshTokenNotValidException extends RuntimeException {
    public RefreshTokenNotValidException() {
        super("Refresh token is not valid");
    }
}
