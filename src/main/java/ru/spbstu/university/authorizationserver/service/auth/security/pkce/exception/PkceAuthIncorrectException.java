package ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception;

public class PkceAuthIncorrectException extends RuntimeException{
    public PkceAuthIncorrectException() {
        super("Pkce auth incorrect");
    }
}
