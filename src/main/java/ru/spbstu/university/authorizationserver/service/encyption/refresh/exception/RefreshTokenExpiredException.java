package ru.spbstu.university.authorizationserver.service.encyption.refresh.exception;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() {
        super("Refresh token is expired");
    }
}
