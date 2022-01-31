package ru.spbstu.university.authorizationserver.service.encyption.pkce.exception;

public class PkceAuthIncorrectException extends RuntimeException{
    public PkceAuthIncorrectException() {
        super("Pkce auth incorrect");
    }
}
