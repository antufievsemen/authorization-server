package ru.spbstu.university.authorizationserver.service.auth.security.token.refresh.exception;

public class RefreshTokenExpiredException extends Exception {
    public RefreshTokenExpiredException() {
        super("Refresh token is expired");
    }
}
