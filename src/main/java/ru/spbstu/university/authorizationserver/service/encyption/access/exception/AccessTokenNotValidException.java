package ru.spbstu.university.authorizationserver.service.encyption.access.exception;

public class AccessTokenNotValidException extends RuntimeException{
    public AccessTokenNotValidException() {
        super("Access token not valid");
    }
}
