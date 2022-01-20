package ru.spbstu.university.authorizationserver.service.token.refresh.exception;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() {
        super("Refresh token is expired");
    }
}
