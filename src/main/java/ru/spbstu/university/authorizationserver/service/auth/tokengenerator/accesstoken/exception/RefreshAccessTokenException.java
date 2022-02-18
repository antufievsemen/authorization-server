package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.accesstoken.exception;

public class RefreshAccessTokenException extends RuntimeException {
    public RefreshAccessTokenException() {
        super("Refresh token or access token miss matched");
    }
}
