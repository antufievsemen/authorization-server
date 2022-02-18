package ru.spbstu.university.authorizationserver.service.exception;

public class CodeVerifierExpiredException extends RuntimeException{
    public CodeVerifierExpiredException() {
        super("Code verifier expired");
    }
}
