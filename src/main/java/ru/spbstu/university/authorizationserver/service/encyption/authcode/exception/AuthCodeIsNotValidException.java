package ru.spbstu.university.authorizationserver.service.encyption.authcode.exception;

public class AuthCodeIsNotValidException extends RuntimeException{
    public AuthCodeIsNotValidException() {
        super("Auth code is expired or not valid");
    }
}
