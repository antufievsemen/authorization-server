package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken.exception;

public class RefreshTokenValidationException extends RuntimeException {
    public RefreshTokenValidationException() {
        super("Refresh token is not valid");
    }
}
