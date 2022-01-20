package ru.spbstu.university.authorizationserver.service.exception;

public class UserRefreshTokenNotFoundException extends RuntimeException{
    public UserRefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
